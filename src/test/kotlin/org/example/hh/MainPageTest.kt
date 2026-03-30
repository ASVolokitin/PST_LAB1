package org.example.hh

import org.example.hh.utils.ConfigReader
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.openqa.selenium.Cookie
import org.openqa.selenium.PageLoadStrategy
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.time.Duration

class MainPageTest {

    private lateinit var driver: WebDriver

    @BeforeEach
    fun setup() {
        val options = ChromeOptions().apply {
            setPageLoadStrategy(PageLoadStrategy.EAGER)
        }
        driver = ChromeDriver(options)
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(300))

        driver.get("https://hh.ru/robots.txt")

        val cookie = Cookie.Builder("hhtoken", ConfigReader.getProperty("hhtoken"))
            .domain(".hh.ru")
            .path("/")
            .build()
        driver.manage().addCookie(cookie)

        driver.navigate().to("https://hh.ru/")
        driver.manage().window().maximize()
    }

    @Test
    fun shouldBeLoggedIn() {
        Thread.sleep(2000)
    }

    @AfterEach
    fun tearDown() {
        if (::driver.isInitialized) {
            driver.quit()
        }
    }
}
