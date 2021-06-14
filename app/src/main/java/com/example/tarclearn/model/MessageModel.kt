package com.example.tarclearn.model

data class MessageDetailDto(
    val messageId: Int,
    val message: String,
    val userId: String,
    val userName: String
)

data class MessageDto(
    val messageId: Int,
    val message: String
)

data class DiscussionMessage(
    val message: String,
    val userId: String,
    val threadId: Int
)