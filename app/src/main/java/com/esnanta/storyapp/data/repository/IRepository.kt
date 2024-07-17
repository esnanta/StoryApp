package com.esnanta.storyapp.data.repository

import com.esnanta.storyapp.data.model.UserModel
import kotlinx.coroutines.flow.Flow

interface IRepository{
    fun getSession(): Flow<UserModel>
    suspend fun logout()
}