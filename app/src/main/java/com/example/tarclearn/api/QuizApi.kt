package com.example.tarclearn.api

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


}