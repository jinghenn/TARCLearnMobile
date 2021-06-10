package com.example.tarclearn.model

data class CourseDetailDto(
    val courseId: Int,
    val courseCode: String,
    val courseTitle: String,
    val courseDescription: String
)

data class CourseDto(
    val courseId: Int,
    val courseCode: String,
    val courseTitle: String
)