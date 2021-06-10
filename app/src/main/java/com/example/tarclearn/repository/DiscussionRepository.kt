package com.example.tarclearn.repository

import com.example.tarclearn.api.RetrofitInstance
import com.example.tarclearn.model.DiscussionAboutDto
import com.example.tarclearn.model.DiscussionThread
import com.example.tarclearn.model.DiscussionThreadDetailDto
import retrofit2.Response

class DiscussionRepository {
    suspend fun deleteDiscussionThread(threadId: Int): Response<DiscussionAboutDto> {
        return RetrofitInstance.discussionApi.deleteDiscussionThread(threadId)
    }

    suspend fun getDiscussionDetail(threadId: Int): Response<DiscussionThreadDetailDto> {
        return RetrofitInstance.discussionApi.getDiscussionDetail(threadId)
    }

    suspend fun updateDiscussionDetail(
        threadId: Int,
        updatedThread: DiscussionAboutDto
    ): Response<DiscussionAboutDto> {
        return RetrofitInstance.discussionApi.updateDiscussionDetail(threadId, updatedThread)
    }

    suspend fun createDiscussionThread(newThread: DiscussionThread):
            Response<DiscussionThreadDetailDto> {
        return RetrofitInstance.discussionApi.createDiscussionThread(newThread)
    }
}