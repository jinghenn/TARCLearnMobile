package com.example.tarclearn.model

data class UserDto(
    val userId: String,
    val username: String,
    val email:String
)

data class UserDetailDto(
    val userId: String,
    var password: String,
    val username: String,
    val email: String,
    val isLecturer: Boolean
)

