package com.mohirdev.kutubxona.data.model

enum class Role {
    ADMIN, USER
}

data class User(
    val id: String,
    val fullName: String,
    val username: String,
    val password: String,
    val role: Role,
    val registeredDate: String
)
