package com.example.tarclearn.viewmodel.discussion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tarclearn.model.DiscussionThreadDto
import com.example.tarclearn.repository.ChapterRepository
import kotlinx.coroutines.launch

class DiscussionListViewModel(val repository: ChapterRepository) : ViewModel() {

    private val _threadList = MutableLiveData<List<DiscussionThreadDto>>()
    val threadList: LiveData<List<DiscussionThreadDto>>
        get() = _threadList

    fun fetchThreadList(chapterId: Int) {
        viewModelScope.launch {
            val response = repository.getChapterDiscussions(chapterId)
            if (response.code() == 200) {
                _threadList.value = response.body()
            }

        }
    }
}