package com.esnanta.storyapp.di

import android.content.Context
import com.esnanta.storyapp.data.StoryRepository
import com.esnanta.storyapp.data.source.remote.api.ApiConfig
import com.esnanta.storyapp.data.source.local.UserPreference
import com.esnanta.storyapp.data.source.local.dataStore
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first

object StoryInjection {
    fun provideRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return StoryRepository.getInstance(pref, apiService)
    }
}