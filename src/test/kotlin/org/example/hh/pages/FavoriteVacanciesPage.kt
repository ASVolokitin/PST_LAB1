package org.example.hh.pages

import org.example.hh.config.TestTimeouts
import org.example.hh.pages.selectors.FavoriteVacanciesPageSelectors
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration

class FavoriteVacanciesPage(private val driver: WebDriver) {

    fun open(): FavoriteVacanciesPage {
        driver.navigate().to(FAVORITE_VACANCIES_URL)
        return this
    }

    fun waitUntilLoaded(timeout: Duration = TestTimeouts.FAVORITE_VACANCIES_PAGE_WAIT): FavoriteVacanciesPage {
        WebDriverWait(driver, timeout).until {
            driver.currentUrl.orEmpty().contains("/applicant/favorite_vacancies") &&
                driver.findElements(By.xpath(FavoriteVacanciesPageSelectors.MAIN_CONTENT_XPATH)).isNotEmpty()
        }
        return this
    }

    fun containsVacancy(vacancyId: String): Boolean {
        val vacancyLinkXPath = FavoriteVacanciesPageSelectors.vacancyLinkByIdXPath(vacancyId)
        return driver.findElements(By.xpath(vacancyLinkXPath)).isNotEmpty()
    }

    fun waitUntilVacancyPresent(
        vacancyId: String,
        timeout: Duration = TestTimeouts.FAVORITE_VACANCIES_PAGE_WAIT,
    ): FavoriteVacanciesPage {
        WebDriverWait(driver, timeout).until { containsVacancy(vacancyId) }
        return this
    }

    fun waitUntilVacancyAbsent(
        vacancyId: String,
        timeout: Duration = TestTimeouts.FAVORITE_VACANCIES_PAGE_WAIT,
    ): FavoriteVacanciesPage {
        WebDriverWait(driver, timeout).until { !containsVacancy(vacancyId) }
        return this
    }

    private companion object {
        const val FAVORITE_VACANCIES_URL = "https://spb.hh.ru/applicant/favorite_vacancies"
    }
}
