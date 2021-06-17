package com.example.tarclearn.viewmodel.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tarclearn.model.Quiz
import com.example.tarclearn.model.QuizQuestionsDto
import com.example.tarclearn.repository.QuizRepository
import kotlinx.coroutines.launch

class ManageQuizViewModel(private val repository: QuizRepository) : ViewModel() {

    private val _quizQuestions = MutableLiveData<QuizQuestionsDto>()
    val quizQuestions: LiveData<QuizQuestionsDto> get() = _quizQuestions

    fun createQuiz(newQuiz: Quiz) {
        viewModelScope.launch {

        }
    }
}