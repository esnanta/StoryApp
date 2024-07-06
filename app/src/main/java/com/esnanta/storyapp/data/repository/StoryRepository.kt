package com.esnanta.storyapp.data.repository

import com.esnanta.storyapp.data.source.local.UserPreference
import com.esnanta.storyapp.data.source.remote.api.ApiService

class StoryRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) : IRepository {

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(userPreference, apiService)
            }.also { instance = it }
    }
}