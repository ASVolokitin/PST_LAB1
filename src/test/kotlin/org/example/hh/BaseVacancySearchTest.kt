package org.example.hh

import io.kotest.assertions.withClue
import io.kotest.core.Tag
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.example.hh.config.VacancyFixture
import org.example.hh.driver.WebDriverFactory
import org.example.hh.pages.SearchResultsPage
import org.example.hh.pages.VacancyPage
import org.openqa.selenium.WebDriver
import org.opentest4j.TestAbortedException

abstract class BaseVacancySearchTest(
    private val userTypeLabel: String,
) : FunSpec() {

    protected lateinit var driver: WebDriver
    
    private lateinit var searchResultsPage: SearchResultsPage
    private lateinit var vacancyPage: VacancyPage

    init {
        beforeTest {
            driver = WebDriverFactory.create()
            searchResultsPage = SearchResultsPage(driver)
            vacancyPage = VacancyPage(driver)
            prepareUserSession()
        }

        test("fixture vacancy should be available by direct link for $userTypeLabel") {
            ensureFixtureVacancyAvailableOrAbort(vacancyPage)
            vacancyPage.hasVacancyId(VacancyFixture.ID) shouldBe true
        }

        test("$userTypeLabel should find fixture vacancy in search results") {
            ensureFixtureVacancyAvailableOrAbort(vacancyPage)

            val resultsPage = searchResultsPage
                .openByQuery(VacancyFixture.SEARCH_QUERY)
                .waitUntilLoaded()

            withClue(
                "Vacancy ${VacancyFixture.ID} was not found for query '${VacancyFixture.SEARCH_QUERY}' " +
                    "for $userTypeLabel. It may have been removed from search index.",
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

    protected open fun prepareUserSession() = Unit

    protected open fun extraTags(): Set<Tag> = emptySet()


    private fun ensureFixtureVacancyAvailableOrAbort(vacancyPage: VacancyPage) {
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
