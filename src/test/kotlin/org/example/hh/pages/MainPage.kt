package org.example.hh.pages

import org.example.hh.config.TestTimeouts
import org.example.hh.pages.selectors.MainPageSelectors
import org.example.hh.pages.selectors.SelectorXPath
import org.openqa.selenium.By
import org.openqa.selenium.TimeoutException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration

class MainPage(private val driver: WebDriver) {

    fun open() {
        driver.navigate().to(BASE_URL)
    }

    fun waitUntilLoggedIn(timeout: Duration = TestTimeouts.LOGGED_IN_WAIT) {
        WebDriverWait(driver, timeout).until { isLoggedIn() }
    }

    fun isLoggedIn(): Boolean {
        return MainPageSelectors.LOGGED_IN_INDICATORS_XPATH
            .all { driver.findElements(By.xpath(it)).isNotEmpty() }
    }

    fun openHeaderSectionAndGetUrl(
        sectionQa: String,
        timeout: Duration = TestTimeouts.PAGE_LOAD,
    ): String {
        val baseHandle = driver.windowHandle
        val beforeUrl = driver.currentUrl.orEmpty()
        val beforeHandles = driver.windowHandles

        clickMainMenuItem(sectionQa)

        WebDriverWait(driver, timeout).until {
            driver.windowHandles.size != beforeHandles.size || driver.currentUrl.orEmpty() != beforeUrl
        }

        if (driver.windowHandles.size > beforeHandles.size) {
            val newHandle = driver.windowHandles.first { !beforeHandles.contains(it) }
            driver.switchTo().window(newHandle)
            val openedUrl = waitForStableUrl(timeout)
            try {
                driver.close()
            } finally {
                driver.switchTo().window(baseHandle)
            }
            return openedUrl
        }

        return driver.currentUrl.orEmpty()
    }

    fun openHelpMenu(timeout: Duration = TestTimeouts.LOGGED_IN_WAIT): MainPage {
        clickMainMenuItem(MainPageSelectors.HELP_QA)
        WebDriverWait(driver, timeout).until { isHelpMenuOpened() }
        return this
    }

    fun isHelpMenuOpened(): Boolean {
        val supportChatVisible = driver.findElements(By.xpath(MainPageSelectors.SUPPORT_CHAT_BUTTON_XPATH))
            .any { it.isDisplayed }
        val findAnswerLinkVisible = driver.findElements(By.xpath(MainPageSelectors.HELP_FIND_ANSWER_LINK_XPATH))
            .any { it.isDisplayed }
        return supportChatVisible && findAnswerLinkVisible
    }

    private fun clickMainMenuItem(sectionQa: String) {
        val sectionXpath = SelectorXPath.byDataQa(sectionQa)

        var sectionElement = driver.findElements(By.xpath(sectionXpath)).firstOrNull { it.isDisplayed }
        if (sectionElement == null) {
            val moreItems = driver.findElements(By.xpath(MainPageSelectors.MORE_ITEMS_XPATH))
                .firstOrNull { it.isDisplayed }
                ?: error("Main menu item '$sectionQa' is not available in header.")

            moreItems.click()
            sectionElement = driver.findElements(By.xpath(sectionXpath)).firstOrNull { it.isDisplayed }
        }

        sectionElement?.click() ?: error("Main menu item '$sectionQa' is not available in header.")
    }

    private fun waitForStableUrl(timeout: Duration): String {
        return try {
            WebDriverWait(driver, timeout).until {
                !isTransientUrl(driver.currentUrl.orEmpty())
            }
            driver.currentUrl.orEmpty()
        } catch (_: TimeoutException) {
            driver.currentUrl.orEmpty()
        }
    }

    private fun isTransientUrl(url: String): Boolean {
        return url.isBlank() || url == ABOUT_BLANK || url == ABOUT_NEW_TAB
    }

    private companion object {
        const val BASE_URL = "https://hh.ru/"
        const val ABOUT_BLANK = "about:blank"
        const val ABOUT_NEW_TAB = "about:newtab"
    }
}
