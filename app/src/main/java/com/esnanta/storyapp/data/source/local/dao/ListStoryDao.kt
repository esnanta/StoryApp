package com.esnanta.storyapp.data.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.paging.PagingSource
import com.esnanta.storyapp.data.source.local.entity.ListStoryEntity

@Dao
interface ListStoryDao : BaseDao<ListStoryEntity> {

    @Query("SELECT * FROM liststoryentity")
    fun getAllStories(): PagingSource<Int, ListStoryEntity>

    @Query("DELETE FROM liststoryentity")
    suspend fun deleteAllStories()
}