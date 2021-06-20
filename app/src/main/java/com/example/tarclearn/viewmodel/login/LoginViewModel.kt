package com.example.tarclearn.viewmodel.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tarclearn.model.UserDetailDto
import com.example.tarclearn.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: UserRepository
) : ViewModel() {

    private val _user = MutableLiveData<UserDetailDto>()
    val user = _user as LiveData<UserDetailDto>

    private val _isAuthenticated = MutableLiveData<Boolean>()
    val isAuthenticated = _isAuthenticated as LiveData<Boolean>
    private val _loginError = MutableLiveData<Boolean>()
    val loginError = _loginError as LiveData<Boolean>

    fun login(userId: String, password: String) {
        viewModelScope.launch {
            val response = repository.getUserById(userId)
            if (response.isSuccessful) {
                _user.value = response.body()
                _isAuthenticated.value = password == _user.value!!.password
                _loginError.value = password != user.value!!.password
            } else {
                _loginError.value = true
            }
        }
    }


}