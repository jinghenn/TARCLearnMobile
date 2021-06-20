package com.example.tarclearn.viewmodel.discussion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tarclearn.model.DiscussionAboutDto
import com.example.tarclearn.model.DiscussionThread
import com.example.tarclearn.model.DiscussionThreadDetailDto
import com.example.tarclearn.repository.DiscussionRepository
import kotlinx.coroutines.launch

class ManageDiscussionViewModel(val repository: DiscussionRepository) : ViewModel() {
    private val _discussionDetail = MutableLiveData<DiscussionThreadDetailDto>()
    val discussionDetail: LiveData<DiscussionThreadDetailDto> get() = _discussionDetail

    private val _successFlag = MutableLiveData<Boolean?>()
    val successFlag: LiveData<Boolean?> get() = _successFlag

    private val _errorFlag = MutableLiveData<Boolean?>()
    val errorFlag: LiveData<Boolean?> get() = _errorFlag

    fun fetchDiscussionDetail(threadId: Int) {
        viewModelScope.launch {
            val response = repository.getDiscussionDetail(threadId)
            if (response.code() == 200) {
                _discussionDetail.value = response.body()

            }
        }
    }

    fun createDiscussionThread(
        threadTitle: String,
        threadDescription: String,
        chapterId: Int,
        userId: String
    ) {
        val newThread = DiscussionThread(threadTitle, threadDescription, chapterId, userId)
        viewModelScope.launch {
            val response = repository.createDiscussionThread(newThread)
            if (response.code() == 201) {
                _discussionDetail.value = response.body()
                _successFlag.value = true
            }
            if (!response.isSuccessful) {
                _errorFlag.value = true
            }
        }
    }

    fun updateDiscussionThread(
        threadId: Int,
        threadTitle: String,
        threadDescription: String,
    ) {
        val newThread = DiscussionAboutDto(threadId, threadTitle, threadDescription)
        viewModelScope.launch {
            val response = repository.updateDiscussionDetail(threadId, newThread)
            if (response.code() == 200) {
                _successFlag.value = true
            }
            if (!response.isSuccessful) {
                _errorFlag.value = true
            }
        }
    }

    fun resetSuccessFlag() {
        _successFlag.value = null
    }

    fun resetErrorFlag() {
        _errorFlag.value = null
    }
}