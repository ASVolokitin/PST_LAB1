package org.example.hh.driver

import org.example.hh.config.TestTimeouts
import org.openqa.selenium.PageLoadStrategy
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions

object WebDriverFactory {

    enum class Browser {
        CHROME,
        FIREFOX,

        ;

        companion object {
            fun from(raw: String?): Browser {
                return when (raw?.trim()?.lowercase()) {
                    "firefox", "ff", "firefix" -> FIREFOX
                    else -> CHROME
                }
            }
        }
    }

    fun create(browser: Browser = resolveBrowser()): WebDriver {
        return when (browser) {
            Browser.CHROME -> createChromeDriver()
            Browser.FIREFOX -> createFirefoxDriver()
        }
    }

    private fun createChromeDriver(): WebDriver {
        val options = ChromeOptions().apply {
            setPageLoadStrategy(PageLoadStrategy.EAGER)
            addArguments("--window-size=1920,1080")
        }

        return setupTimeouts(ChromeDriver(options))
    }

    private fun createFirefoxDriver(): WebDriver {
        val options = FirefoxOptions().apply {
            setPageLoadStrategy(PageLoadStrategy.EAGER)
            addArguments("--width=1920")
            addArguments("--height=1080")
            addPreference("network.protocol-handler.external.tg", false)
            addPreference("network.protocol-handler.warn-external.tg", false)
            addPreference("network.protocol-handler.expose.tg", false)
            addPreference("dom.webnotifications.enabled", false)
            addPreference("permissions.default.desktop-notification", 2)
        }

        return setupTimeouts(FirefoxDriver(options))
    }

    private fun setupTimeouts(driver: WebDriver): WebDriver {
        return driver.also {
            driver.manage().timeouts().apply {
                pageLoadTimeout(TestTimeouts.PAGE_LOAD)
                scriptTimeout(TestTimeouts.SCRIPT)
                implicitlyWait(TestTimeouts.IMPLICIT)
            }
        }
    }

    private fun resolveBrowser(): Browser {
        val browserFromProperty = System.getProperty(BROWSER_PROPERTY)
        val browserFromEnv = System.getenv(BROWSER_ENV)
        return Browser.from(browserFromProperty ?: browserFromEnv)
    }

    private const val BROWSER_PROPERTY = "browser"
    private const val BROWSER_ENV = "BROWSER"
}
