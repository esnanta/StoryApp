package com.esnanta.storyapp.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.esnanta.storyapp.BuildConfig
import com.esnanta.storyapp.data.source.local.dao.ListStoryDao
import com.esnanta.storyapp.data.source.local.dao.UserDao
import com.esnanta.storyapp.data.source.local.entity.UserEntity

@Database(entities = [UserEntity::class], version = BuildConfig.DATABASE_VERSION_CODE, exportSchema = false)
abstract class StoryAppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun listStoryDao(): ListStoryDao

    companion object {
        @Volatile
        private var instance: StoryAppDatabase? = null
        fun getInstance(context: Context): StoryAppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    StoryAppDatabase::class.java, BuildConfig.DATABASE_NAME
                ).build()
            }
    }
}
