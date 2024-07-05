package com.esnanta.storyapp.di

import android.content.Context
import com.esnanta.storyapp.data.StoryRepository
import com.esnanta.storyapp.data.UserRepository
import com.esnanta.storyapp.data.source.remote.api.ApiConfig
import com.esnanta.storyapp.data.source.local.UserPreference
import com.esnanta.storyapp.data.source.local.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object UserInjection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(pref, apiService)
    }
}