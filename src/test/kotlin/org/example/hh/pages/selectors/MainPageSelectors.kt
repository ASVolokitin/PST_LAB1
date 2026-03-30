package org.example.hh.pages.selectors

object MainPageSelectors {
    const val PROFILE_AND_RESUMES_XPATH = "//*[@data-qa='mainmenu_profileAndResumes']"
    const val NOTIFICATIONS_XPATH = "//*[@data-qa='mainmenu_notifications']"
    const val VACANCY_RESPONSES_XPATH = "//*[@data-qa='mainmenu_vacancyResponses']"

    val LOGGED_IN_INDICATORS_XPATH = listOf(
        PROFILE_AND_RESUMES_XPATH,
        NOTIFICATIONS_XPATH,
        VACANCY_RESPONSES_XPATH,
    )
}
