package org.example.hh

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.example.hh.auth.AuthSession
import org.example.hh.driver.WebDriverFactory
import org.example.hh.pages.MainPage
import org.example.hh.pages.selectors.MainPageSelectors
import org.openqa.selenium.WebDriver

class MainMenuNavigationTest : FunSpec() {
    private lateinit var driver: WebDriver
    private lateinit var mainPage: MainPage

    init {
        beforeTest {
            driver = WebDriverFactory.create()
            mainPage = MainPage(driver)
            AuthSession.authorizeByHhToken(driver)
            mainPage.open()
            mainPage.waitUntilLoggedIn()
            mainPage.isLoggedIn() shouldBe true
        }

        test("header item 'Резюме и профиль' should open resumes page") {
            val openedUrl = mainPage.openHeaderSectionAndGetUrl(MainPageSelectors.PROFILE_AND_RESUMES_QA)
            openedUrl shouldContain "/applicant/resumes"
        }

        test("header item 'Отклики' should open negotiations page") {
            val openedUrl = mainPage.openHeaderSectionAndGetUrl(MainPageSelectors.VACANCY_RESPONSES_QA)
            openedUrl shouldContain "/applicant/negotiations"
        }

        test("header item 'Сервисы' should open services page") {
            val openedUrl = mainPage.openHeaderSectionAndGetUrl(MainPageSelectors.ALL_SERVICES_QA)
            openedUrl shouldContain "/services"
        }

        test("header item 'Карьера' should open career page") {
            val openedUrl = mainPage.openHeaderSectionAndGetUrl(MainPageSelectors.CAREER_QA)
            openedUrl shouldContain "career.hh.ru"
        }

        test("header item 'Помощь' should open help menu") {
            mainPage.openHelpMenu()
            mainPage.isHelpMenuOpened() shouldBe true
        }

        afterTest {
            if (::driver.isInitialized) {
                driver.quit()
            }
        }
    }
}
