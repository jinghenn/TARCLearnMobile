package com.example.tarclearn.model

data class UserDto(
    val userId: String,
    val username: String
)
data class UserDetailDto (
    val userId: String,
    var password: String,
    val username: String,
    val isLecturer: Boolean
)

data class UserCourseDto(
    val courseId: String,
    val courseName: String
)