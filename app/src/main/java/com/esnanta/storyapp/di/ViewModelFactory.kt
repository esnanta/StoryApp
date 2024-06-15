package com.esnanta.storyapp.di

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory private constructor(private val mApplication: Application)
    : ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(application: Application): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(application)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(FavoriteListViewModel::class.java)) {
//            return FavoriteListViewModel(mApplication) as T
//        }
//
//        else if (modelClass.isAssignableFrom(FavoriteDetailViewModel::class.java)) {
//            return FavoriteDetailViewModel(mApplication) as T
//        }
//
//        else if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
//            return UserViewModel(mApplication) as T
//        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}