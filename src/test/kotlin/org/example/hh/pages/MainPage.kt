package org.example.hh.pages

import org.example.hh.config.TestTimeouts
import org.example.hh.pages.selectors.MainPageSelectors
import org.openqa.selenium.By
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

    private companion object {
        const val BASE_URL = "https://hh.ru/"
    }
}
