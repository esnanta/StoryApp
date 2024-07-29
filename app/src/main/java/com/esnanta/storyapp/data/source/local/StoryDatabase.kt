package com.esnanta.storyapp.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.esnanta.storyapp.BuildConfig
import com.esnanta.storyapp.data.source.local.dao.ListStoryDao
import com.esnanta.storyapp.data.source.local.dao.UserDao
import com.esnanta.storyapp.data.source.local.entity.ListStoryEntity
import com.esnanta.storyapp.data.source.local.entity.UserEntity

@Database(entities = [UserEntity::class, ListStoryEntity::class],
    version = 1, exportSchema = false)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun listStoryDao(): ListStoryDao

    companion object {
        @Volatile
        private var instance: StoryDatabase? = null
        fun getInstance(context: Context): StoryDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    StoryDatabase::class.java, BuildConfig.DATABASE_NAME
                ).build()
            }
    }
}
