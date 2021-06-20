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

    private val _successFlag = MutableLiveData<Boolean?>()
    val successFlag: LiveData<Boolean?> get() = _successFlag

    fun createQuiz(newQuiz: Quiz) {
        viewModelScope.launch {
            val response = repository.createQuiz(newQuiz)
            if (response.code() == 201) {
                _quizQuestions.value = response.body()
                _successFlag.value = true
            }
        }
    }

    fun fetchQuizQuestions(quizId: Int) {
        viewModelScope.launch {
            val response = repository.getQuizQuestions(quizId)
            if (response.code() == 200) {
                _quizQuestions.value = response.body()
            }

        }
    }

    fun updateQuiz(quizId: Int, updatedQuiz: Quiz) {
        viewModelScope.launch {
            val response = repository.updateQuiz(quizId, updatedQuiz)
            if (response.code() == 200) {
                _successFlag.value = true
            }
        }
    }

    fun resetSuccessFlag() {
        _successFlag.value = null
    }
}