package org.example.hh

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.example.hh.config.TestTimeouts
import org.example.hh.pages.MainPage
import org.example.hh.utils.ConfigReader
import org.openqa.selenium.Cookie
import org.openqa.selenium.PageLoadStrategy
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

class MainPageTest : FunSpec() {
    private lateinit var driver: WebDriver
    private lateinit var mainPage: MainPage

    init {
        beforeTest {
            val options = ChromeOptions().apply {
                setPageLoadStrategy(PageLoadStrategy.EAGER)
                addArguments("--window-size=1920,1080")
            }
            driver = ChromeDriver(options)
            driver.manage().timeouts().apply {
                pageLoadTimeout(TestTimeouts.PAGE_LOAD)
                scriptTimeout(TestTimeouts.SCRIPT)
                implicitlyWait(TestTimeouts.IMPLICIT)
            }

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
