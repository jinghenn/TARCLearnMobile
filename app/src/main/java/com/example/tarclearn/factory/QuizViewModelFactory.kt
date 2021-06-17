package com.example.tarclearn.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tarclearn.repository.QuizRepository
import com.example.tarclearn.viewmodel.quiz.ManageQuizViewModel
import com.example.tarclearn.viewmodel.quiz.QuizViewModel

class QuizViewModelFactory(
    private val repository: QuizRepository
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
            return QuizViewModel(repository) as T
        }
        if(modelClass.isAssignableFrom(ManageQuizViewModel::class.java)){
            return ManageQuizViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}