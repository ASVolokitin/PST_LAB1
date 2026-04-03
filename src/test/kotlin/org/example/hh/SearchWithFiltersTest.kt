package org.example.hh

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.example.hh.config.VacancyFixture
import org.example.hh.driver.WebDriverFactory
import org.example.hh.pages.SearchResultsPage
import org.openqa.selenium.WebDriver

class SearchWithFiltersTest : FunSpec() {
    private lateinit var driver: WebDriver
    private lateinit var searchResultsPage: SearchResultsPage

    init {
        beforeTest {
            driver = WebDriverFactory.create()
            searchResultsPage = SearchResultsPage(driver)
        }

        test("search results should show filters and apply 'Удаленная работа' filter") {
            searchResultsPage
                .openByQuery(VacancyFixture.SEARCH_QUERY)
                .waitUntilLoaded()

            searchResultsPage.hasQuickFilters() shouldBe true

            searchResultsPage.applyQuickFilter("udalennaya_rabota")
            searchResultsPage.currentUrl() shouldContain "/vacancies/udalennaya_rabota"
        }

        test("search results should show filters and apply 'Без опыта работы' filter") {
            searchResultsPage
                .openByQuery(VacancyFixture.SEARCH_QUERY)
                .waitUntilLoaded()

            searchResultsPage.hasQuickFilters() shouldBe true

            searchResultsPage.applyQuickFilter("bez_opyta_raboty")
            searchResultsPage.currentUrl() shouldContain "/vacancies/bez_opyta_raboty"
        }

        afterTest {
            if (::driver.isInitialized) {
                driver.quit()
            }
        }
    }
}
