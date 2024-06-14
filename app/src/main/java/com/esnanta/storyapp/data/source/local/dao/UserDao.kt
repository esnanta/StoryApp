package com.esnanta.storyapp.data.source.local.dao

import androidx.room.Dao
import com.esnanta.storyapp.data.source.local.entity.UserEntity

@Dao
interface UserDao : BaseDao<UserEntity>