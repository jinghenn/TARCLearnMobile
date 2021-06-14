package com.example.tarclearn.viewmodel.discussion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tarclearn.model.DiscussionMessage
import com.example.tarclearn.model.DiscussionThreadDetailDto
import com.example.tarclearn.model.MessageDetailDto
import com.example.tarclearn.repository.DiscussionRepository
import com.example.tarclearn.repository.MessageRepository
import kotlinx.coroutines.launch

class DiscussionViewModel(
    private val discussionRepository: DiscussionRepository,
    private val messageRepository: MessageRepository
) : ViewModel() {
    private val _discussionDetail = MutableLiveData<DiscussionThreadDetailDto>()
    val discussionDetail: LiveData<DiscussionThreadDetailDto>
        get() = _discussionDetail

    private val _messages = MutableLiveData<MutableList<MessageDetailDto>>()
    val messages: LiveData<MutableList<MessageDetailDto>>
        get() = _messages

    private val _successFlag = MutableLiveData<Boolean?>()
    val successFlag: LiveData<Boolean?> get() = _successFlag

    fun fetchDiscussionThreadDetail(threadId: Int) {
        viewModelScope.launch {
            val response = discussionRepository.getDiscussionDetail(threadId)
            if (response.code() == 200) {
                _discussionDetail.value = response.body()

            }
            val messageResponse = discussionRepository.getDiscussionMessages(threadId)
            if (messageResponse.code() == 200) {
                _messages.value = messageResponse.body() as MutableList
            }
        }
    }

    fun deleteDiscussionThread(threadId: Int) {
        viewModelScope.launch {
            val response = discussionRepository.deleteDiscussionThread(threadId)
            if (response.code() == 200) {
                _successFlag.value = true
            }
        }
    }

    fun sendMessage(message: String, userId: String, threadId: Int) {
        viewModelScope.launch {
            val response =
                messageRepository.createMessage(DiscussionMessage(message, userId, threadId))
            if (response.code() == 201) {
                val temp = _messages.value
                temp?.let {
                    it.add(response.body()!!)
                    _messages.value = it
                }
            }
        }
    }


}