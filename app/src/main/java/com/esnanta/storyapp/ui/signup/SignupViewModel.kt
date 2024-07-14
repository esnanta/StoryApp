package com.esnanta.storyapp.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esnanta.storyapp.data.repository.UserRepository
import com.esnanta.storyapp.data.source.remote.response.RegisterResponse
import com.esnanta.storyapp.data.source.remote.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignupViewModel(private val repository: UserRepository) : ViewModel() {

    private val _registerResult = MutableLiveData<Result<RegisterResponse>>()
    val registerResult: LiveData<Result<RegisterResponse>> get() = _registerResult

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _dialogMessage = MutableLiveData<String?>()
    val dialogMessage: LiveData<String?> get() = _dialogMessage

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _registerResult.value = Result.Loading
            withContext(Dispatchers.IO) {
                val result = repository.registerSession(name, email, password)
                _registerResult.postValue(result)
            }
            _isLoading.value = false
        }
    }

    fun onRegistrationSuccess(message: String) {
        _dialogMessage.value = message
    }

    fun onRegistrationError(error: String) {
        _dialogMessage.value = error
    }

    fun clearDialogMessage() {
        _dialogMessage.value = null
    }
}