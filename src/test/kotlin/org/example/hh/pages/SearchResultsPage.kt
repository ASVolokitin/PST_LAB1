package org.example.hh.pages

import org.example.hh.config.TestTimeouts
import org.example.hh.pages.selectors.SearchResultsPageSelectors
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.WebDriverWait
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.Duration

class SearchResultsPage(private val driver: WebDriver) {

    fun openByQuery(query: String): SearchResultsPage {
        val encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8)
        driver.navigate().to("$BASE_URL/search/vacancy?text=$encodedQuery&search_field=name")
        return this
    }

    fun waitUntilLoaded(timeout: Duration = TestTimeouts.SEARCH_RESULTS_WAIT): SearchResultsPage {
        WebDriverWait(driver, timeout).until {
            driver.currentUrl.orEmpty().contains("/search/vacancy") &&
                driver.findElements(By.xpath(SearchResultsPageSelectors.MAIN_CONTENT_XPATH)).isNotEmpty()
        }
        return this
    }

    fun containsVacancy(vacancyId: String): Boolean {
        val vacancyLinkXPath = SearchResultsPageSelectors.vacancyLinkByIdXPath(vacancyId)
        return driver.findElements(By.xpath(vacancyLinkXPath)).isNotEmpty()
    }

    fun openVacancy(vacancyId: String): VacancyPage {
        val vacancyLinkXPath = SearchResultsPageSelectors.vacancyLinkByIdXPath(vacancyId)
        val vacancyLink = driver.findElement(By.xpath(vacancyLinkXPath))
        val href = vacancyLink.getDomAttribute("href")
            ?: error("Vacancy link for id=$vacancyId does not have href")
        driver.navigate().to(href)
        return VacancyPage(driver)
    }

    private companion object {
        const val BASE_URL = "https://hh.ru"
    }
}
