package org.example.hh.pages

import org.example.hh.config.TestTimeouts
import org.example.hh.pages.selectors.VacancyPageSelectors
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.WebDriverWait
import java.net.URI
import java.time.Duration

class VacancyPage(private val driver: WebDriver) {

    fun open(vacancyUrl: String): VacancyPage {
        driver.navigate().to(vacancyUrl)
        return this
    }

    fun waitUntilLoaded(timeout: Duration = TestTimeouts.VACANCY_PAGE_WAIT): VacancyPage {
        WebDriverWait(driver, timeout).until {
            driver.findElements(By.xpath(VacancyPageSelectors.VACANCY_TITLE_XPATH)).isNotEmpty() &&
                driver.findElements(By.xpath(VacancyPageSelectors.VACANCY_DESCRIPTION_XPATH)).isNotEmpty()
        }
        return this
    }

    fun hasVacancyId(vacancyId: String): Boolean {
        return currentVacancyId() == vacancyId
    }

    fun isInFavorites(): Boolean {
        return when (val state = favoriteQaState()) {
            VacancyPageSelectors.FAVORITE_TRUE_QA -> true
            VacancyPageSelectors.FAVORITE_FALSE_QA -> false
            null -> error("Favorite toggle not found on vacancy page.")
            else -> error("Unexpected favorite toggle state '$state'.")
        }
    }

    fun addToFavorites(timeout: Duration = TestTimeouts.VACANCY_PAGE_WAIT): VacancyPage {
        val initialState = favoriteQaState()
            ?: error("Cannot add to favorites: favorite toggle not found on vacancy page.")

        if (initialState == VacancyPageSelectors.FAVORITE_TRUE_QA) {
            error("Cannot add to favorites: vacancy is already in favorites.")
        }
        if (initialState != VacancyPageSelectors.FAVORITE_FALSE_QA) {
            error("Cannot add to favorites: unexpected initial favorite state '$initialState'.")
        }

        clickFavoriteToggle()
        waitFavoriteState(
            expectedQa = VacancyPageSelectors.FAVORITE_TRUE_QA,
            timeout = timeout,
        )
        return this
    }

    fun removeFromFavorites(timeout: Duration = TestTimeouts.VACANCY_PAGE_WAIT): VacancyPage {
        val initialState = favoriteQaState()
            ?: error("Cannot remove from favorites: favorite toggle not found on vacancy page.")

        if (initialState == VacancyPageSelectors.FAVORITE_FALSE_QA) {
            error("Cannot remove from favorites: vacancy is not in favorites.")
        }
        if (initialState != VacancyPageSelectors.FAVORITE_TRUE_QA) {
            error("Cannot remove from favorites: unexpected initial favorite state '$initialState'.")
        }

        clickFavoriteToggle()
        waitFavoriteState(
            expectedQa = VacancyPageSelectors.FAVORITE_FALSE_QA,
            timeout = timeout,
        )
        return this
    }

    fun currentVacancyId(): String? {
        val currentUrl = driver.currentUrl ?: return null
        return VACANCY_ID_REGEX.find(currentUrl)?.groupValues?.get(1)
    }

    fun isRedirectedToVacancies(): Boolean {
        val currentUrl = driver.currentUrl ?: return false
        val path = runCatching { URI(currentUrl).path }.getOrNull().orEmpty()
        return path == "/vacancies" || path.startsWith("/vacancies/")
    }

    private fun clickFavoriteToggle() {
        val toggle = driver.findElement(By.xpath(VacancyPageSelectors.FAVORITE_TOGGLE_XPATH))
        toggle.click()
    }

    private fun waitFavoriteState(expectedQa: String, timeout: Duration) {
        WebDriverWait(driver, timeout).until {
            favoriteQaState() == expectedQa
        }
    }

    private fun favoriteQaState(): String? {
        return driver.findElements(By.xpath(VacancyPageSelectors.FAVORITE_TOGGLE_XPATH))
            .firstOrNull()
            ?.getDomAttribute("data-qa")
    }

    private companion object {
        val VACANCY_ID_REGEX = Regex("/vacancy/(\\d+)")
    }
}
