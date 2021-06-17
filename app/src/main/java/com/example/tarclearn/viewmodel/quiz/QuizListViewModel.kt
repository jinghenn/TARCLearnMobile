package com.example.tarclearn.viewmodel.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tarclearn.model.MaterialDetailDto
import com.example.tarclearn.model.QuizDto
import com.example.tarclearn.repository.ChapterRepository
import kotlinx.coroutines.launch

class QuizListViewModel(private val repository: ChapterRepository) : ViewModel(){
    private val _quizList = MutableLiveData<List<QuizDto>>()
    val quizList: LiveData<List<QuizDto>>
        get() = _quizList

    fun fetchQuizList(chapterId: Int) {
        viewModelScope.launch {
            val response = repository.getChapterQuizzes(chapterId)
            if (response.code() == 200) {
                _quizList.value = response.body()
            }
        }
    }
}