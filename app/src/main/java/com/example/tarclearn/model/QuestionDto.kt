package com.example.tarclearn.model

data class QuestionDto(
    val questionId: Int,
    val questionText: String,
    val choices: MutableList<ChoiceDto>
)

data class Question(
    var questionText: String,
    val Choices: MutableList<Choice>
)