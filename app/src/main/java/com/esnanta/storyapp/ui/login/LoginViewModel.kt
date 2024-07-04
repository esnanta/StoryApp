package com.esnanta.storyapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.esnanta.storyapp.data.UserRepository
import com.esnanta.storyapp.data.model.UserModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _userSession = MutableLiveData<UserModel>()
    val userSession: LiveData<UserModel> get() = _userSession

    init {
        getSession()
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