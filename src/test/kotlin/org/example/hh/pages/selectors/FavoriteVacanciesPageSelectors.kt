package org.example.hh.pages.selectors

object FavoriteVacanciesPageSelectors {
    const val MAIN_CONTENT_QA = "main-content"
    val MAIN_CONTENT_XPATH = SelectorXPath.byDataQa(MAIN_CONTENT_QA)

    fun vacancyLinkByIdXPath(vacancyId: String): String {
        return "//a[contains(@href, '/vacancy/$vacancyId')]"
    }
}
