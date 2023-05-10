package com.khtn.teachproject.helper

class SupportLanguage(val lang: String = "") {
    companion object {
        val VIETNAM = SupportLanguage("vi")
        val ENGLISH = SupportLanguage("en")
    }
}