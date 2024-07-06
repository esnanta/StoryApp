package com.esnanta.storyapp.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.esnanta.storyapp.data.repository.IRepository
import com.esnanta.storyapp.data.repository.StoryRepository
import com.esnanta.storyapp.ui.login.LoginViewModel
import com.esnanta.storyapp.ui.main.MainViewModel
import com.esnanta.storyapp.data.repository.UserRepository
import com.esnanta.storyapp.ui.signup.SignupViewModel
import com.esnanta.storyapp.ui.story.ListStoryViewModel

class ViewModelFactory(private val repository: IRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository as UserRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository as UserRepository) as T
            }
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(repository as UserRepository) as T
            }
            modelClass.isAssignableFrom(ListStoryViewModel::class.java) -> {
                ListStoryViewModel(repository as StoryRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context, repositoryType: Class<out IRepository>): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactory(
                    Injection.provideRepository(context, repositoryType)
                )
            }.also { INSTANCE = it }
        }
    }
}