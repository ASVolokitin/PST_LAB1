package org.example.hh.auth

import org.example.hh.utils.ConfigReader
import org.openqa.selenium.Cookie
import org.openqa.selenium.WebDriver

object AuthSession {

    fun authorizeByHhToken(driver: WebDriver) {
        driver.get("https://hh.ru/robots.txt")

        val cookie = Cookie.Builder("hhtoken", ConfigReader.getProperty("hhtoken"))
            .domain(".hh.ru")
            .path("/")
            .isSecure(true)
            .build()
        driver.manage().addCookie(cookie)
    }
}
