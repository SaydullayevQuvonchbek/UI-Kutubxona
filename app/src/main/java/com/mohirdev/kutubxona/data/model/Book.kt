package com.mohirdev.kutubxona.data.model

data class Book(
    val id: String,
    var title: String,
    var author: String,
    var genre: String,
    var totalCopies: Int,
    var availableCopies: Int,
    var description: String
)
