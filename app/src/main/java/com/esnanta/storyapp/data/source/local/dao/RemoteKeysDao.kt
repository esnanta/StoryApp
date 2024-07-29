package com.esnanta.storyapp.data.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.esnanta.storyapp.data.source.local.entity.RemoteKeysEntity

@Dao
interface RemoteKeysDao : BaseDao<RemoteKeysEntity>{

    @Query("SELECT * FROM remote_keys WHERE id = :id")
    suspend fun getRemoteKeysId(id: String): RemoteKeysEntity?

    @Query("DELETE FROM remote_keys")
    suspend fun deleteRemoteKeys()
}