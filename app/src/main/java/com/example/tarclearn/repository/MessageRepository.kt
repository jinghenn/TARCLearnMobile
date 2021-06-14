package com.example.tarclearn.repository

import com.example.tarclearn.api.RetrofitInstance
import com.example.tarclearn.model.DiscussionMessage
import com.example.tarclearn.model.MessageDetailDto
import com.example.tarclearn.model.MessageDto
import retrofit2.Response

class MessageRepository {
    suspend fun getMessage(messageId: Int): Response<MessageDetailDto> {
        return RetrofitInstance.messageApi.getMessage(messageId)
    }

    suspend fun updateMessage(
        messageId: Int,
        updatedMessage: MessageDto
    ): Response<MessageDetailDto> {
        return RetrofitInstance.messageApi.updateMessage(messageId, updatedMessage)
    }

    suspend fun createMessage(newMessage: DiscussionMessage): Response<MessageDetailDto> {
        return RetrofitInstance.messageApi.createMessage(newMessage)
    }

    suspend fun deleteMessage(messageId: Int): Response<MessageDetailDto> {
        return RetrofitInstance.messageApi.deleteMessage(messageId)
    }
}