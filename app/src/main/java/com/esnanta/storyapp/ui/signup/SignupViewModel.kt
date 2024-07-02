package com.esnanta.storyapp.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esnanta.storyapp.data.UserRepository
import com.esnanta.storyapp.data.model.UserModel
import kotlinx.coroutines.launch

class SignupViewModel(private val repository: UserRepository) : ViewModel() {
    fun registerSession(user: UserModel) {
        viewModelScope.launch {
            repository.registerSession(user)
        }
    }
}