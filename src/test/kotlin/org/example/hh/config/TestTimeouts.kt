package org.example.hh.config

import java.time.Duration

object TestTimeouts {
    val PAGE_LOAD: Duration = Duration.ofSeconds(20)
    val SCRIPT: Duration = Duration.ofSeconds(10)
    val IMPLICIT: Duration = Duration.ZERO
    val LOGGED_IN_WAIT: Duration = Duration.ofSeconds(10)
    val SEARCH_RESULTS_WAIT: Duration = Duration.ofSeconds(10)
    val VACANCY_PAGE_WAIT: Duration = Duration.ofSeconds(10)
}
