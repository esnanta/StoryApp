package com.esnanta.storyapp.di

import android.content.Context
import com.esnanta.storyapp.data.repository.IRepository
import com.esnanta.storyapp.data.repository.StoryRepository
import com.esnanta.storyapp.data.repository.UploadRepository
import com.esnanta.storyapp.data.repository.UserRepository
import com.esnanta.storyapp.data.source.remote.api.ApiConfig
import com.esnanta.storyapp.data.source.local.UserPreference
import com.esnanta.storyapp.data.source.local.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(pref, apiService)
    }

    fun provideStoryRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return StoryRepository.getInstance(pref, apiService)
    }

    fun provideAddStoryRepository(context: Context): UploadRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return UploadRepository.getInstance(pref, apiService)
    }
}