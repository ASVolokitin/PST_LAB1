package org.example.hh

import io.kotest.matchers.shouldBe
import io.kotest.core.spec.style.FunSpec
import org.example.hh.auth.AuthSession
import org.example.hh.config.VacancyFixture
import org.example.hh.driver.WebDriverFactory
import org.example.hh.pages.FavoriteVacanciesPage
import org.example.hh.pages.MainPage
import org.example.hh.pages.VacancyPage
import org.openqa.selenium.WebDriver
import org.opentest4j.TestAbortedException

class LoggedInFavoriteVacancyTest : FunSpec() {

    private lateinit var driver: WebDriver
    private lateinit var mainPage: MainPage
    private lateinit var vacancyPage: VacancyPage
    private lateinit var favoriteVacanciesPage: FavoriteVacanciesPage

    init {
        beforeTest {
            driver = WebDriverFactory.create()
            mainPage = MainPage(driver)
            vacancyPage = VacancyPage(driver)
            favoriteVacanciesPage = FavoriteVacanciesPage(driver)

            AuthSession.authorizeByHhToken(driver)
            mainPage.open()
            mainPage.waitUntilLoggedIn()
            mainPage.isLoggedIn() shouldBe true

            vacancyPage.open(VacancyFixture.URL)
            if (vacancyPage.isRedirectedToVacancies()) {
                throw TestAbortedException(
                    "Fixture vacancy ${VacancyFixture.URL} redirects to /vacancies, so favorite tests are skipped.",
                )
            }

            vacancyPage.waitUntilLoaded()
            vacancyPage.hasVacancyId(VacancyFixture.ID) shouldBe true
        }

        test("logged in user should add vacancy to favorites") {
            vacancyPage.addToFavorites()
            vacancyPage.isInFavorites() shouldBe true

            favoriteVacanciesPage
                .open()
                .waitUntilLoaded()
                .waitUntilVacancyPresent(VacancyFixture.ID)
            favoriteVacanciesPage.containsVacancy(VacancyFixture.ID) shouldBe true
        }

        test("logged in user should remove vacancy from favorites") {
            vacancyPage.removeFromFavorites()
            vacancyPage.isInFavorites() shouldBe false

            favoriteVacanciesPage
                .open()
                .waitUntilLoaded()
                .waitUntilVacancyAbsent(VacancyFixture.ID)
            favoriteVacanciesPage.containsVacancy(VacancyFixture.ID) shouldBe false
        }

        afterTest {
            if (::driver.isInitialized) {
                driver.quit()
            }
        }
    }
}
