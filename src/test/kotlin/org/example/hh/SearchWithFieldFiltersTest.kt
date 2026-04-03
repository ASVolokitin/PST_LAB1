package org.example.hh

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.example.hh.config.VacancyFixture
import org.example.hh.driver.WebDriverFactory
import org.example.hh.pages.SearchResultsPage
import org.openqa.selenium.WebDriver

class SearchWithFieldFiltersTest : FunSpec() {
    private lateinit var driver: WebDriver
    private lateinit var searchResultsPage: SearchResultsPage

    init {
        beforeTest {
            driver = WebDriverFactory.create()
            searchResultsPage = SearchResultsPage(driver)
        }

        test("advanced search should apply specialization filter") {
            searchResultsPage
                .openByQuery(VacancyFixture.SEARCH_QUERY)
                .waitUntilLoaded()
                .openAdvancedSearch()
                .selectSpecialization("Информационные технологии")

            searchResultsPage.selectedSpecializationText() shouldContain "Изменить"

            searchResultsPage
                .submitAdvancedSearch()
                .waitUntilLoaded()

            searchResultsPage.currentUrl() shouldContain "professional_role="
        }

        test("advanced search should apply region and salary filters") {
            searchResultsPage
                .openByQuery(VacancyFixture.SEARCH_QUERY)
                .waitUntilLoaded()
                .openAdvancedSearch()
                .setRegion("Санкт-Петербург")
                .setSalaryFrom("250000")
                .submitAdvancedSearch()
                .waitUntilLoaded()

            val currentUrl = searchResultsPage.currentUrl()
            currentUrl shouldContain "area=2"
            currentUrl shouldContain "salary=250000"
            currentUrl.contains("/search/vacancy") shouldBe true
        }

        afterTest {
            if (::driver.isInitialized) {
                driver.quit()
            }
        }
    }
}
