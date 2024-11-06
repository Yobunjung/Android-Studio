package com.eseul.yobunjeong.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eseul.yobunjeong.model.LoginResponse
import com.eseul.yobunjeong.repository.LoginRepository

class LoginViewModel : ViewModel() {
    private val loginRepository = LoginRepository()

    private val _loginResult = MutableLiveData<LoginResponse?>()
    val loginResult: LiveData<LoginResponse?> get() = _loginResult

    fun loginUser(email: String, password: String) {
        loginRepository.loginUser(email, password) { response ->
            _loginResult.postValue(response)
        }
    }
}
