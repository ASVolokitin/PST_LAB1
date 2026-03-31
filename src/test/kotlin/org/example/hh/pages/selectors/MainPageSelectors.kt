package org.example.hh.pages.selectors

object MainPageSelectors {
    const val PROFILE_AND_RESUMES_QA = "mainmenu_profileAndResumes"
    const val NOTIFICATIONS_QA = "mainmenu_notifications"
    const val VACANCY_RESPONSES_QA = "mainmenu_vacancyResponses"
    const val ALL_SERVICES_QA = "mainmenu_allServices"
    const val CAREER_QA = "mainmenu_careerhhru"
    const val HELP_QA = "mainmenu_help"
    const val MORE_ITEMS_QA = "mainmenu_moreItems"
    const val SUPPORT_CHAT_BUTTON_QA = "support-chat-button"

    val PROFILE_AND_RESUMES_XPATH = SelectorXPath.byDataQa(PROFILE_AND_RESUMES_QA)
    val NOTIFICATIONS_XPATH = SelectorXPath.byDataQa(NOTIFICATIONS_QA)
    val VACANCY_RESPONSES_XPATH = SelectorXPath.byDataQa(VACANCY_RESPONSES_QA)
    val ALL_SERVICES_XPATH = SelectorXPath.byDataQa(ALL_SERVICES_QA)
    val CAREER_XPATH = SelectorXPath.byDataQa(CAREER_QA)
    val HELP_XPATH = SelectorXPath.byDataQa(HELP_QA)
    val MORE_ITEMS_XPATH = SelectorXPath.byDataQa(MORE_ITEMS_QA)
    val SUPPORT_CHAT_BUTTON_XPATH = SelectorXPath.byDataQa(SUPPORT_CHAT_BUTTON_QA)
    const val HELP_FIND_ANSWER_LINK_XPATH = "//a[contains(normalize-space(.), 'Найти ответ')]"

    val LOGGED_IN_INDICATORS_XPATH = listOf(
        PROFILE_AND_RESUMES_XPATH,
        NOTIFICATIONS_XPATH,
        VACANCY_RESPONSES_XPATH,
    )
}
