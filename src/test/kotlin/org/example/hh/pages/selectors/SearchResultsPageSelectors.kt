package org.example.hh.pages.selectors

object SearchResultsPageSelectors {
    const val MAIN_CONTENT_QA = "main-content"
    const val VACANCY_SERP_ITEM_QA = "vacancy-serp__vacancy"
    const val QUICK_FILTERS_QA = "catalog-search-extra-filters"
    const val ADVANCED_SEARCH_QA = "advanced-search"
    const val ADVANCED_FORM_QA = "advanced-vacancy-search__form"
    const val PROFROLES_SWITCHER_QA = "resumesearch__profroles-switcher"
    const val PROFROLES_SWITCHER_TEXT_QA = "resumesearch__profroles-switcher-text"
    const val MODAL_OVERLAY_QA = "modal-overlay"
    const val REGION_INPUT_QA = "advanced-search-region-add"
    const val REGION_SELECTED_QA = "advanced-search__selected-regions"
    const val SALARY_INPUT_QA = "advanced-search-salary"
    const val ADVANCED_SUBMIT_QA = "advanced-search-submit-button"
    const val SUGGEST_ITEM_QA = "suggest-item-cell"

    val MAIN_CONTENT_XPATH = SelectorXPath.byDataQa(MAIN_CONTENT_QA)
    val QUICK_FILTERS_XPATH = SelectorXPath.byDataQa(QUICK_FILTERS_QA)
    val ADVANCED_SEARCH_XPATH = SelectorXPath.byDataQa(ADVANCED_SEARCH_QA)
    val ADVANCED_FORM_XPATH = SelectorXPath.byDataQa(ADVANCED_FORM_QA)
    val PROFROLES_SWITCHER_XPATH = SelectorXPath.byDataQa(PROFROLES_SWITCHER_QA)
    val PROFROLES_SWITCHER_TEXT_XPATH = SelectorXPath.byDataQa(PROFROLES_SWITCHER_TEXT_QA)
    val MODAL_OVERLAY_XPATH = SelectorXPath.byDataQa(MODAL_OVERLAY_QA)
    val REGION_INPUT_XPATH = SelectorXPath.byDataQa(REGION_INPUT_QA)
    val REGION_SELECTED_XPATH = SelectorXPath.byDataQa(REGION_SELECTED_QA)
    val SALARY_INPUT_XPATH = SelectorXPath.byDataQa(SALARY_INPUT_QA)
    val ADVANCED_SUBMIT_XPATH = SelectorXPath.byDataQa(ADVANCED_SUBMIT_QA)

    fun vacancyLinkByIdXPath(vacancyId: String): String {
        return "${SelectorXPath.byDataQa(VACANCY_SERP_ITEM_QA)}//a[contains(@href, '/vacancy/$vacancyId')]"
    }

    fun quickFilterXPath(filterName: String): String {
        return "${SelectorXPath.byDataQa(QUICK_FILTERS_QA)}//a[contains(@href, '/vacancies/$filterName')]"
    }

    fun specializationOptionByTextXPath(specializationText: String): String {
        return "//*[normalize-space(.)='$specializationText']"
    }

    fun suggestionItemByTextXPath(itemText: String): String {
        return "${SelectorXPath.byDataQa(SUGGEST_ITEM_QA)}[contains(normalize-space(.), '$itemText')]"
    }

    fun buttonByTextXPath(buttonText: String): String {
        return "//button[normalize-space(.)='$buttonText']"
    }
}
