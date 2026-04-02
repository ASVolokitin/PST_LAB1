package org.example.hh.pages

import org.example.hh.config.TestTimeouts
import org.example.hh.pages.selectors.VacancyPageSelectors
import org.openqa.selenium.By
import org.openqa.selenium.ElementClickInterceptedException
import org.openqa.selenium.ElementNotInteractableException
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
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
        return when (favoriteState()) {
            FavoriteState.IN_FAVORITES -> true
            FavoriteState.NOT_IN_FAVORITES -> false
            null -> error("Favorite toggle state is not available on vacancy page.")
        }
    }

    fun addToFavorites(timeout: Duration = TestTimeouts.VACANCY_PAGE_WAIT): VacancyPage {
        val initialState = favoriteState()
            ?: error("Cannot add to favorites: favorite toggle state is unavailable.")

        if (initialState == FavoriteState.IN_FAVORITES) {
            return this
        }

        clickFavoriteToggle(fromState = FavoriteState.NOT_IN_FAVORITES)
        waitFavoriteState(
            expectedState = FavoriteState.IN_FAVORITES,
            timeout = timeout,
        )
        return this
    }

    fun removeFromFavorites(timeout: Duration = TestTimeouts.VACANCY_PAGE_WAIT): VacancyPage {
        val initialState = favoriteState()
            ?: error("Cannot remove from favorites: favorite toggle state is unavailable.")

        if (initialState == FavoriteState.NOT_IN_FAVORITES) {
            return this
        }

        clickFavoriteToggle(fromState = FavoriteState.IN_FAVORITES)
        waitFavoriteState(
            expectedState = FavoriteState.NOT_IN_FAVORITES,
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

    private fun clickFavoriteToggle(fromState: FavoriteState) {
        val toggle = favoriteToggles()
            .firstOrNull { it.isDisplayed && it.isEnabled && favoriteStateFrom(it) == fromState }
            ?: favoriteToggles().firstOrNull { it.isDisplayed && it.isEnabled }
            ?: error("Favorite toggle not found on vacancy page.")

        (driver as JavascriptExecutor).executeScript(
            "arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});",
            toggle,
        )

        try {
            toggle.click()
        } catch (_: ElementClickInterceptedException) {
            (driver as JavascriptExecutor).executeScript("arguments[0].click();", toggle)
        } catch (_: ElementNotInteractableException) {
            (driver as JavascriptExecutor).executeScript("arguments[0].click();", toggle)
        }
    }

    private fun waitFavoriteState(expectedState: FavoriteState, timeout: Duration) {
        WebDriverWait(driver, timeout).until {
            favoriteState() == expectedState
        }
    }

    private fun favoriteState(): FavoriteState? {
        val toggles = favoriteToggles()
        if (toggles.isEmpty()) return null

        val prioritized = toggles
            .filter { it.isDisplayed && it.isEnabled }
            .ifEmpty { toggles.filter { it.isDisplayed } }
            .ifEmpty { toggles }

        return prioritized
            .asSequence()
            .mapNotNull { favoriteStateFrom(it) }
            .firstOrNull()
    }

    private fun favoriteStateFrom(toggle: WebElement): FavoriteState? {
        val ariaPressed = runCatching { toggle.getDomAttribute("aria-pressed") }
            .getOrNull()
            ?.trim()
            ?.lowercase()

        if (ariaPressed == "true") return FavoriteState.IN_FAVORITES
        if (ariaPressed == "false") return FavoriteState.NOT_IN_FAVORITES

        val dataQa = runCatching { toggle.getDomAttribute("data-qa") }
            .getOrNull()
            ?.trim()
            .orEmpty()

        if (dataQa.endsWith("_true") || dataQa.contains("_true_")) {
            return FavoriteState.IN_FAVORITES
        }
        if (dataQa.endsWith("_false") || dataQa.contains("_false_")) {
            return FavoriteState.NOT_IN_FAVORITES
        }

        return null
    }

    private fun favoriteToggles(): List<WebElement> {
        return driver.findElements(By.xpath(VacancyPageSelectors.FAVORITE_TOGGLE_XPATH))
    }

    private enum class FavoriteState {
        IN_FAVORITES,
        NOT_IN_FAVORITES,
    }

    private companion object {
        val VACANCY_ID_REGEX = Regex("/vacancy/(\\d+)")
    }
}
