package org.example.hh.pages.selectors

object SelectorXPath {
    fun byDataQa(qa: String): String = "//*[@data-qa='$qa']"

    fun byDataQaPrefix(prefix: String): String = "//*[starts-with(@data-qa, '$prefix')]"
}
