package com.esnanta.storyapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esnanta.storyapp.data.repository.UserRepository
import com.esnanta.storyapp.data.model.UserModel
import com.esnanta.storyapp.data.source.remote.Result
import com.esnanta.storyapp.data.source.remote.response.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
            withContext(Dispatchers.IO) {
                val result = repository.loginSession(email, password)
                _loginResult.postValue(result)
                if (result is Result.Success) {
                    repository.getSession().collect { user ->
                        _userSession.postValue(user)
                    }
                }
            }
            _isLoading.value = false
        }
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.saveSession(user)
            }
        }
    }

    private fun getSession() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.getSession().collect { user ->
                    _userSession.postValue(user)
                }
            }
        }
    }

    fun getCurrentUser(): UserModel? {
        return _userSession.value
    }
}
