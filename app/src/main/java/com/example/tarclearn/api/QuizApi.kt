package com.example.tarclearn.api

import androidx.room.Update
import com.example.tarclearn.model.QuestionDto
import com.example.tarclearn.model.Quiz
import com.example.tarclearn.model.QuizQuestionsDto
import retrofit2.Response
import retrofit2.http.*

interface QuizApi {
    @GET("quiz/{id}")
    suspend fun getQuizQuestions(@Path("id") quizId: Int): Response<QuizQuestionsDto>

    @POST("quiz")
    suspend fun createQuiz(@Body newQuiz: Quiz): Response<QuizQuestionsDto>

    @DELETE("quiz/{id}")
    suspend fun deleteQuiz(@Path("id") quizId: Int):Response<QuizQuestionsDto>

    @PUT("quiz/{id}")
    suspend fun updateQuiz(@Path("id") quizId: Int, @Body updatedQuiz: Quiz):Response<QuizQuestionsDto>
}