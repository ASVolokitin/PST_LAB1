package org.example.hh.pages.selectors

object VacancyPageSelectors {
    const val VACANCY_TITLE_QA = "vacancy-title"
    const val VACANCY_DESCRIPTION_QA = "vacancy-description"
    const val FAVORITE_TOGGLE_QA_PREFIX = "vacancy-body-mark-favorite_"
    const val FAVORITE_FALSE_QA = "vacancy-body-mark-favorite_false"
    const val FAVORITE_TRUE_QA = "vacancy-body-mark-favorite_true"

    val VACANCY_TITLE_XPATH = SelectorXPath.byDataQa(VACANCY_TITLE_QA)
    val VACANCY_DESCRIPTION_XPATH = SelectorXPath.byDataQa(VACANCY_DESCRIPTION_QA)
    val FAVORITE_TOGGLE_XPATH = SelectorXPath.byDataQaPrefix(FAVORITE_TOGGLE_QA_PREFIX)
}
