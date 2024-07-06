package com.esnanta.storyapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esnanta.storyapp.data.repository.UserRepository
import com.esnanta.storyapp.data.model.UserModel
import com.esnanta.storyapp.data.source.remote.Result
import com.esnanta.storyapp.data.source.remote.response.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _userSession = MutableLiveData<UserModel>()
    val userSession: LiveData<UserModel> get() = _userSession

    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> get() = _loginResult

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _dialogMessage = MutableLiveData<String?>()
    val dialogMessage: LiveData<String?> = _dialogMessage

    init {
        getSession()
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.loginSession(email, password)
            _isLoading.value = false
            _loginResult.postValue(result)
            if (result is Result.Success) {
                repository.getSession().collect { user ->
                    _userSession.postValue(user)
                }
            }
        }
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun getSession() {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                _userSession.postValue(user)
            }
        }
    }

    fun getCurrentUser(): UserModel? {
        return _userSession.value
    }
}
