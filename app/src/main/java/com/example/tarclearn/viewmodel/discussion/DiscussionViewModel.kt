package com.example.tarclearn.viewmodel.discussion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tarclearn.api.RetrofitInstance
import com.example.tarclearn.model.DiscussionThreadDetailDto
import com.example.tarclearn.model.UserDto
import com.example.tarclearn.repository.DiscussionRepository
import kotlinx.coroutines.launch

class DiscussionViewModel(val repository: DiscussionRepository) : ViewModel() {
    private val _discussionDetail = MutableLiveData<DiscussionThreadDetailDto>()
    val discussionDetail: LiveData<DiscussionThreadDetailDto>
        get() = _discussionDetail

    private val _successFlag = MutableLiveData<Boolean?>()
    val successFlag: LiveData<Boolean?> get() = _successFlag

    fun fetchDiscussionThreadDetail(threadId: Int){
        viewModelScope.launch {
            val response = repository.getDiscussionDetail(threadId)
            if(response.code() == 200){
                _discussionDetail.value = response.body()

            }
        }
    }

    fun deleteDiscussionThread(threadId: Int){
        viewModelScope.launch {
            val response = repository.deleteDiscussionThread(threadId)
            if(response.code() == 200){
                _successFlag.value = true
            }
        }
    }


}