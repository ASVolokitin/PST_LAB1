package org.example.hh

import io.kotest.matchers.shouldBe
import org.example.hh.auth.AuthSession
import org.example.hh.pages.MainPage

class LoggedInVacancySearchTest : BaseVacancySearchTest(
    userTypeLabel = "logged in user",
) {
    private lateinit var mainPage: MainPage

    override fun prepareUserSession() {
        AuthSession.authorizeByHhToken(driver)
        mainPage = MainPage(driver)
        mainPage.open()
        mainPage.waitUntilLoggedIn()
        mainPage.isLoggedIn() shouldBe true
    }
}
