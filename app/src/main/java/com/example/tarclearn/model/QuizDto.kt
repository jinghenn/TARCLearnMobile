package com.example.tarclearn.model

data class QuizDto(
    val quizId: Int,
    val quizTitle: String
)
data class QuizQuestionsDto(
    val quizId: Int,
    val quizTitle: String,
    val questions: List<QuestionDto>
)

data class Quiz(
    val quizTitle: String,
    val Questions: List<Question>,
    val chapterId: Int
)