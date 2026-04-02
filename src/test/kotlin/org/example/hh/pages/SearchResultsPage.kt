package org.example.hh.pages

import org.example.hh.config.TestTimeouts
import org.example.hh.pages.selectors.SearchResultsPageSelectors
import org.openqa.selenium.By
import org.openqa.selenium.ElementClickInterceptedException
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
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

    fun hasQuickFilters(): Boolean {
        return driver.findElements(By.xpath(SearchResultsPageSelectors.QUICK_FILTERS_XPATH)).isNotEmpty()
    }

    fun applyQuickFilter(
        filterName: String,
        timeout: Duration = TestTimeouts.SEARCH_RESULTS_WAIT,
    ): SearchResultsPage {
        val beforeUrl = driver.currentUrl.orEmpty()
        val quickFilterXPath = SearchResultsPageSelectors.quickFilterXPath(filterName)
        val quickFilterBy = By.xpath(quickFilterXPath)
        val quickFilterLink = WebDriverWait(driver, timeout)
            .until(ExpectedConditions.elementToBeClickable(quickFilterBy))

        try {
            quickFilterLink.click()
        } catch (_: ElementClickInterceptedException) {
            (driver as JavascriptExecutor).executeScript("arguments[0].click();", quickFilterLink)
        }

        WebDriverWait(driver, timeout).until {
            driver.currentUrl.orEmpty() != beforeUrl &&
                driver.currentUrl.orEmpty().contains("/vacancies/$filterName") &&
                driver.findElements(By.xpath(SearchResultsPageSelectors.MAIN_CONTENT_XPATH)).isNotEmpty()
        }

        return this
    }

    fun openAdvancedSearch(timeout: Duration = TestTimeouts.SEARCH_RESULTS_WAIT): SearchResultsPage {
        val beforeUrl = driver.currentUrl.orEmpty()
        val advancedSearchBy = By.xpath(SearchResultsPageSelectors.ADVANCED_SEARCH_XPATH)
        val advancedSearchButton = WebDriverWait(driver, timeout)
            .until(ExpectedConditions.elementToBeClickable(advancedSearchBy))

        advancedSearchButton.click()

        WebDriverWait(driver, timeout).until {
            val currentUrl = driver.currentUrl.orEmpty()
            currentUrl != beforeUrl &&
                currentUrl.contains("/search/vacancy/advanced") &&
                driver.findElements(By.xpath(SearchResultsPageSelectors.ADVANCED_FORM_XPATH)).isNotEmpty()
        }

        return this
    }

    fun selectSpecialization(
        specializationText: String,
        timeout: Duration = TestTimeouts.SEARCH_RESULTS_WAIT,
    ): SearchResultsPage {
        val switcherBy = By.xpath(SearchResultsPageSelectors.PROFROLES_SWITCHER_XPATH)
        val switcher = WebDriverWait(driver, timeout)
            .until(ExpectedConditions.elementToBeClickable(switcherBy))

        switcher.click()

        val optionBy = By.xpath(SearchResultsPageSelectors.specializationOptionByTextXPath(specializationText))
        val option = WebDriverWait(driver, timeout)
            .until(ExpectedConditions.visibilityOfElementLocated(optionBy))
        option.click()

        val applyBy = By.xpath(SearchResultsPageSelectors.buttonByTextXPath("Применить"))
        val applyButton = WebDriverWait(driver, timeout)
            .until(ExpectedConditions.elementToBeClickable(applyBy))
        applyButton.click()

        WebDriverWait(driver, timeout).until {
            driver.findElements(By.xpath(SearchResultsPageSelectors.MODAL_OVERLAY_XPATH)).isEmpty() &&
                selectedSpecializationText().contains("Изменить")
        }

        return this
    }

    fun setRegion(
        regionName: String,
        timeout: Duration = TestTimeouts.SEARCH_RESULTS_WAIT,
    ): SearchResultsPage {
        val regionInputBy = By.xpath(SearchResultsPageSelectors.REGION_INPUT_XPATH)
        val regionInput = WebDriverWait(driver, timeout)
            .until(ExpectedConditions.visibilityOfElementLocated(regionInputBy))

        regionInput.clear()
        regionInput.sendKeys(regionName)

        val suggestionBy = By.xpath(SearchResultsPageSelectors.suggestionItemByTextXPath(regionName))
        val suggestion = WebDriverWait(driver, timeout)
            .until(ExpectedConditions.elementToBeClickable(suggestionBy))
        suggestion.click()

        WebDriverWait(driver, timeout).until {
            selectedRegionText().contains(regionName)
        }

        return this
    }

    fun setSalaryFrom(salary: String, timeout: Duration = TestTimeouts.SEARCH_RESULTS_WAIT): SearchResultsPage {
        val salaryInputBy = By.xpath(SearchResultsPageSelectors.SALARY_INPUT_XPATH)
        val salaryInput = WebDriverWait(driver, timeout)
            .until(ExpectedConditions.visibilityOfElementLocated(salaryInputBy))

        salaryInput.clear()
        salaryInput.sendKeys(salary)
        return this
    }

    fun submitAdvancedSearch(timeout: Duration = TestTimeouts.SEARCH_RESULTS_WAIT): SearchResultsPage {
        val beforeUrl = driver.currentUrl.orEmpty()
        val submitBy = By.xpath(SearchResultsPageSelectors.ADVANCED_SUBMIT_XPATH)
        val submitButton = WebDriverWait(driver, timeout)
            .until(ExpectedConditions.elementToBeClickable(submitBy))

        submitButton.click()

        WebDriverWait(driver, timeout).until {
            val currentUrl = driver.currentUrl.orEmpty()
            currentUrl != beforeUrl &&
                !currentUrl.contains("/search/vacancy/advanced") &&
                driver.findElements(By.xpath(SearchResultsPageSelectors.MAIN_CONTENT_XPATH)).isNotEmpty()
        }

        return this
    }

    fun selectedSpecializationText(): String {
        return driver.findElements(By.xpath(SearchResultsPageSelectors.PROFROLES_SWITCHER_TEXT_XPATH))
            .firstOrNull()
            ?.text
            .orEmpty()
    }

    fun selectedRegionText(): String {
        return driver.findElements(By.xpath(SearchResultsPageSelectors.REGION_SELECTED_XPATH))
            .firstOrNull()
            ?.text
            .orEmpty()
    }

    fun currentUrl(): String = driver.currentUrl.orEmpty()

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
