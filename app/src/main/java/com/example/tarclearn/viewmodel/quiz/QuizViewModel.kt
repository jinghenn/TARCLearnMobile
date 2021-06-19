package com.example.tarclearn.viewmodel.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tarclearn.model.QuestionDto
import com.example.tarclearn.model.QuizQuestionsDto
import com.example.tarclearn.repository.QuizRepository
import kotlinx.coroutines.launch

class QuizViewModel(val repository: QuizRepository) : ViewModel() {
    private val _quizTitle = MutableLiveData<String>()
    val quizTitle: LiveData<String> get() = _quizTitle

    private val _quizQuestion = MutableLiveData<QuizQuestionsDto>()
    val quizQuestions: LiveData<QuizQuestionsDto> get() = _quizQuestion

    private val _questionList = MutableLiveData<List<QuestionDto>>()
    val questionList: LiveData<List<QuestionDto>> get() = _questionList

    fun fetchQuizQuestions(quizId: Int) {
        viewModelScope.launch {
            val response = repository.getQuizQuestions(quizId)
            if (response.code() == 200) {
                _quizQuestion.value = response.body()
                //_questionList.value = response.body()?.questions
                //_quizTitle.value = response.body()?.quizTitle
            }

        }
    }
}