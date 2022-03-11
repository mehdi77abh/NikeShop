package com.example.nikeshop.data.DataClass

data class Comment(
    val author: Author,
    val content: String,
    val date: String,
    val id: Int,
    val title: String
)