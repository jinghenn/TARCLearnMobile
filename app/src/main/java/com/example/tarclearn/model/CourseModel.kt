package com.example.tarclearn.model

data class CourseDetailDto(
    val courseId: String,
    val courseTitle: String,
    val courseDescription: String
)

data class CourseDto(
    val courseId: String,
    val courseTitle: String
)