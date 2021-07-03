package com.example.tarclearn.viewmodel.discussion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tarclearn.model.DiscussionThreadDto
import com.example.tarclearn.repository.UserRepository
import kotlinx.coroutines.launch

class MyDiscussionsViewModel(val repository: UserRepository) : ViewModel() {
    private val _threadList = MutableLiveData<List<DiscussionThreadDto>>()
    val threadList: LiveData<List<DiscussionThreadDto>>
        get() = _threadList

    fun fetchThreadList(userId: String) {
        viewModelScope.launch {
            val response = repository.getUserDiscussions(userId)
            if (response.code() == 200) {
                _threadList.value = response.body()
            }

        }
    }
}