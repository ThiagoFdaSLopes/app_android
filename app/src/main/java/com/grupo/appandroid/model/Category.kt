package com.grupo.appandroid.model

data class Category(
    val tag: String,
    val label: String
)

data class CategoryResponse(
    val results: List<Category>
)