package org.example.hh

import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.example.hh.config.VacancyFixture
import org.example.hh.driver.WebDriverFactory
import org.example.hh.pages.SearchResultsPage
import org.example.hh.pages.VacancyPage
import org.openqa.selenium.WebDriver
import org.opentest4j.TestAbortedException

class AnonymousVacancySearchTest : FunSpec() {
    private lateinit var driver: WebDriver
    private lateinit var searchResultsPage: SearchResultsPage
    private lateinit var vacancyPage: VacancyPage

    init {
        beforeTest {
            driver = WebDriverFactory.create()
            searchResultsPage = SearchResultsPage(driver)
            vacancyPage = VacancyPage(driver)
        }

        test("fixture vacancy should be available by direct link") {
            ensureFixtureVacancyAvailableOrAbort()
            vacancyPage.hasVacancyId(VacancyFixture.ID) shouldBe true
        }

        test("anonymous user should find fixture vacancy in search results") {
            ensureFixtureVacancyAvailableOrAbort()

            val resultsPage = searchResultsPage
                .openByQuery(VacancyFixture.SEARCH_QUERY)
                .waitUntilLoaded()

            withClue(
                "Vacancy ${VacancyFixture.ID} was not found for query '${VacancyFixture.SEARCH_QUERY}'. " +
                    "It may have been removed from search index.",
            ) {
                resultsPage.containsVacancy(VacancyFixture.ID) shouldBe true
            }

            val openedVacancyPage = resultsPage.openVacancy(VacancyFixture.ID).waitUntilLoaded()
            openedVacancyPage.hasVacancyId(VacancyFixture.ID) shouldBe true
        }

        afterTest {
            if (::driver.isInitialized) {
                driver.quit()
            }
        }
    }

    private fun ensureFixtureVacancyAvailableOrAbort() {
        vacancyPage.open(VacancyFixture.URL)

        if (vacancyPage.isRedirectedToVacancies()) {
            throw TestAbortedException(
                "Fixture vacancy ${VacancyFixture.URL} redirects to /vacancies, so it is removed/archived. " +
                    "Search tests are skipped until fixture is updated.",
            )
        }

        vacancyPage.waitUntilLoaded()
        vacancyPage.hasVacancyId(VacancyFixture.ID) shouldBe true
    }
}
