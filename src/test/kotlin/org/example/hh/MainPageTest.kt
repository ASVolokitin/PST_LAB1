package org.example.hh

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.example.hh.driver.WebDriverFactory
import org.example.hh.pages.MainPage
import org.example.hh.utils.ConfigReader
import org.openqa.selenium.Cookie
import org.openqa.selenium.WebDriver

class MainPageTest : FunSpec() {
    private lateinit var driver: WebDriver
    private lateinit var mainPage: MainPage

    init {
        beforeTest {
            driver = WebDriverFactory.create()

            driver.get("https://hh.ru/robots.txt")

            val cookie = Cookie.Builder("hhtoken", ConfigReader.getProperty("hhtoken"))
                .domain(".hh.ru")
                .path("/")
                .isSecure(true)
                .build()
            driver.manage().addCookie(cookie)

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
