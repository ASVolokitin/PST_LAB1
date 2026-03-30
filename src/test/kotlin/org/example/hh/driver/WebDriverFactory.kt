package org.example.hh.driver

import org.example.hh.config.TestTimeouts
import org.openqa.selenium.PageLoadStrategy
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

object WebDriverFactory {

    fun create(): WebDriver {
        val options = ChromeOptions().apply {
            setPageLoadStrategy(PageLoadStrategy.EAGER)
            addArguments("--window-size=1920,1080")
        }

        return ChromeDriver(options).also { driver ->
            driver.manage().timeouts().apply {
                pageLoadTimeout(TestTimeouts.PAGE_LOAD)
                scriptTimeout(TestTimeouts.SCRIPT)
                implicitlyWait(TestTimeouts.IMPLICIT)
            }
        }
    }
}
