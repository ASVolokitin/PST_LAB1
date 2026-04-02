package org.example.hh.pages.selectors

object SearchResultsPageSelectors {
    const val MAIN_CONTENT_QA = "main-content"
    const val VACANCY_SERP_ITEM_QA = "vacancy-serp__vacancy"
    const val QUICK_FILTERS_QA = "catalog-search-extra-filters"

    val MAIN_CONTENT_XPATH = SelectorXPath.byDataQa(MAIN_CONTENT_QA)
    val QUICK_FILTERS_XPATH = SelectorXPath.byDataQa(QUICK_FILTERS_QA)

    fun vacancyLinkByIdXPath(vacancyId: String): String {
        return "${SelectorXPath.byDataQa(VACANCY_SERP_ITEM_QA)}//a[contains(@href, '/vacancy/$vacancyId')]"
    }

    fun quickFilterXPath(filterName: String): String {
        return "${SelectorXPath.byDataQa(QUICK_FILTERS_QA)}//a[contains(@href, '/vacancies/$filterName')]"
    }
}
