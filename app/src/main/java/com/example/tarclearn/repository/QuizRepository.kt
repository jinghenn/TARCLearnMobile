package com.example.tarclearn.repository

import com.example.tarclearn.api.RetrofitInstance
import com.example.tarclearn.model.Quiz
import com.example.tarclearn.model.QuizQuestionsDto
import retrofit2.Response

class QuizRepository {
    suspend fun getQuizQuestions(quizId: Int): Response<QuizQuestionsDto> {
        return RetrofitInstance.quizApi.getQuizQuestions(quizId)
    }

    suspend fun createQuiz(newQuiz: Quiz): Response<QuizQuestionsDto> {
        return RetrofitInstance.quizApi.createQuiz(newQuiz)
    }

    suspend fun deleteQuiz(quizId: Int): Response<QuizQuestionsDto> {
        return RetrofitInstance.quizApi.deleteQuiz(quizId)
    }

    suspend fun updateQuiz(quizId: Int, updatedQuiz: Quiz): Response<QuizQuestionsDto> {
        return RetrofitInstance.quizApi.updateQuiz(quizId, updatedQuiz)
    }
}