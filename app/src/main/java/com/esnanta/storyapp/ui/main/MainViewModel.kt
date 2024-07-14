package com.esnanta.storyapp.ui.main

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.esnanta.storyapp.R
import com.esnanta.storyapp.data.repository.UserRepository
import com.esnanta.storyapp.data.model.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun showLogoutConfirmationDialog(context: Context, onLogoutConfirmed: () -> Unit) {
        AlertDialog.Builder(context)
            .setTitle(R.string.logout_confirmation_title)
            .setMessage(R.string.logout_confirmation_message)
            .setPositiveButton(R.string.yes) { dialog, _ ->
                logout()
                onLogoutConfirmed()
                dialog.dismiss()
            }
            .setNegativeButton(R.string.no) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun logout() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.logout()
            }
        }
    }
}