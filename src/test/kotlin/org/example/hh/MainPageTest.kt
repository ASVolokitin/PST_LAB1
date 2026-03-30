package org.example.hh

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.example.hh.auth.AuthSession
import org.example.hh.driver.WebDriverFactory
import org.example.hh.pages.MainPage
import org.openqa.selenium.WebDriver
  
class MainPageTest : FunSpec() {
    private lateinit var driver: WebDriver
    private lateinit var mainPage: MainPage

    init {
        beforeTest {
            driver = WebDriverFactory.create()
            runCatching { AuthSession.authorizeByHhToken(driver) }
                .getOrElse { error ->
                    throw AssertionError("Cannot authorize by hhtoken cookie in setup: ${error.message}", error)
                }

            mainPage = MainPage(driver)
        }

        test("shouldBeLoggedIn") {
            mainPage.open()
            mainPage.waitUntilLoggedIn()
            mainPage.isLoggedIn() shouldBe true
        }

        afterTest {
            if (::driver.isInitialized) {
                driver.quit()
            }
        }
    }

}
