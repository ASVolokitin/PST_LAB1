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

    fun currentVacancyId(): String? {
        val currentUrl = driver.currentUrl ?: return null
        return VACANCY_ID_REGEX.find(currentUrl)?.groupValues?.get(1)
    }

    fun isRedirectedToVacancies(): Boolean {
        val currentUrl = driver.currentUrl ?: return false
        val path = runCatching { URI(currentUrl).path }.getOrNull().orEmpty()
        return path == "/vacancies" || path.startsWith("/vacancies/")
    }

    private companion object {
        val VACANCY_ID_REGEX = Regex("/vacancy/(\\d+)")
    }
}
