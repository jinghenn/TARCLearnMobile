package com.example.tarclearn.model

data class DiscussionThreadDto(
    val threadId: Int,
    val threadTitle: String,
    val userName: String
)

data class DiscussionThreadDetailDto(
    val threadId: Int,
    val threadTitle: String,
    val threadDescription: String,
    val chapterId: Int,
    val userId: String,
    val userName: String
)

data class DiscussionAboutDto(
    val threadId: Int,
    val threadTitle: String,
    val threadDescription: String
)

data class DiscussionThread(
    val threadTitle: String,
    val threadDescription: String,
    val chapterId: Int,
    val userId: String
)