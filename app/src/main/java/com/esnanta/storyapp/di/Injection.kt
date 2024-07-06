package com.esnanta.storyapp.di

import android.content.Context
import com.esnanta.storyapp.data.repository.IRepository
import com.esnanta.storyapp.data.repository.StoryRepository
import com.esnanta.storyapp.data.repository.UserRepository
import com.esnanta.storyapp.data.source.remote.api.ApiConfig
import com.esnanta.storyapp.data.source.local.UserPreference
import com.esnanta.storyapp.data.source.local.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    private fun provideUserRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(pref, apiService)
    }

    private fun provideStoryRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return StoryRepository.getInstance(pref, apiService)
    }

    fun provideRepository(context: Context, repositoryType: Class<out IRepository>): IRepository {
        return when (repositoryType) {
            StoryRepository::class.java -> provideStoryRepository(context)
            UserRepository::class.java -> provideUserRepository(context)
            else -> throw IllegalArgumentException("Unknown repository type: ${repositoryType.name}")
        }
    }
}