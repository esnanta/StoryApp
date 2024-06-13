package com.esnanta.storyapp.di

import android.content.Context
import com.esnanta.storyapp.data.UserRepository
import com.esnanta.storyapp.data.source.local.UserPreference
import com.esnanta.storyapp.data.source.local.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}