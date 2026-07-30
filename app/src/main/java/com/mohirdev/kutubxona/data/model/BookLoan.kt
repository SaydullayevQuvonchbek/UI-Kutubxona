package com.mohirdev.kutubxona.data.model

data class BookLoan(
    val id: String,
    val bookId: String,
    val bookTitle: String,
    val bookAuthor: String,
    val userId: String,
    val userFullName: String,
    val borrowDate: String,
    val dueDate: String,
    val dueTimestamp: Long,
    var isReturned: Boolean = false,
    var returnedDate: String? = null
) {
    fun isOverdue(): Boolean {
        return !isReturned && System.currentTimeMillis() > dueTimestamp
    }
}
