package com.kreggscode.einsteinquotes.model

data class Category(
    val name: String,
    val quoteCount: Int,
    val icon: String = "💡"
)
