package org.example.hh.pages.selectors

object MainPageSelectors {
    const val PROFILE_AND_RESUMES_QA = "mainmenu_profileAndResumes"
    const val NOTIFICATIONS_QA = "mainmenu_notifications"
    const val VACANCY_RESPONSES_QA = "mainmenu_vacancyResponses"

    val PROFILE_AND_RESUMES_XPATH = SelectorXPath.byDataQa(PROFILE_AND_RESUMES_QA)
    val NOTIFICATIONS_XPATH = SelectorXPath.byDataQa(NOTIFICATIONS_QA)
    val VACANCY_RESPONSES_XPATH = SelectorXPath.byDataQa(VACANCY_RESPONSES_QA)

    val LOGGED_IN_INDICATORS_XPATH = listOf(
        PROFILE_AND_RESUMES_XPATH,
        NOTIFICATIONS_XPATH,
        VACANCY_RESPONSES_XPATH,
    )
}
