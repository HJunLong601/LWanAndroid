package com.hjl.language.impl

data class LanguageBean(
    val language: String,
    val name: String,
    var isSelect: Boolean = false
)