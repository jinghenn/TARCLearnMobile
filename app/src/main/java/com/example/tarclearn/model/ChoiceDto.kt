package com.example.tarclearn.model

data class ChoiceDto(
    val choiceId: Int,
    val choiceText: String,
    val isAnswer: Boolean
)

data class Choice(
    var choiceText: String,
    var isAnswer: Boolean
)