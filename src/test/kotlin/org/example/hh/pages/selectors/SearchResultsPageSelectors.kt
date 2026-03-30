package org.example.hh.pages.selectors

object SearchResultsPageSelectors {
    const val MAIN_CONTENT_XPATH = "//*[@data-qa='main-content']"
    const val VACANCY_CARD_XPATH = "//*[@data-qa='vacancy-serp__vacancy']"

    fun vacancyLinkByIdXPath(vacancyId: String): String {
        return "//*[@data-qa='vacancy-serp__vacancy']//a[contains(@href, '/vacancy/$vacancyId')]"
    }
}
