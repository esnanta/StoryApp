package com.esnanta.storyapp.data.source.local.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<T>): List<Long>

    @Delete
    suspend fun delete(item: T): Int

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(item: T): Int
}