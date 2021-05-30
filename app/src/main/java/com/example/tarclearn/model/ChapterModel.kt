package com.example.tarclearn.model

data class ChapterDto(
    val chapterNo: String,
    val chapterTitle: String
)
data class ChapterDetailDto(
    val chapterId: Int,
    val chapterNo: String,
    val chapterTitle: String
)

