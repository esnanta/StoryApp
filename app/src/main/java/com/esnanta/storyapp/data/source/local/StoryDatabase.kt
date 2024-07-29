package com.esnanta.storyapp.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.esnanta.storyapp.BuildConfig
import com.esnanta.storyapp.data.source.local.dao.ListStoryDao
import com.esnanta.storyapp.data.source.local.dao.RemoteKeysDao
import com.esnanta.storyapp.data.source.local.dao.UserDao
import com.esnanta.storyapp.data.source.local.entity.ListStoryEntity
import com.esnanta.storyapp.data.source.local.entity.RemoteKeysEntity
import com.esnanta.storyapp.data.source.local.entity.UserEntity

@Database(entities = [UserEntity::class, ListStoryEntity::class, RemoteKeysEntity::class],
    version = 2, exportSchema = false)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun listStoryDao(): ListStoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var instance: StoryDatabase? = null
        fun getInstance(context: Context): StoryDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    StoryDatabase::class.java, BuildConfig.DATABASE_NAME
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                    .also { instance = it }
            }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Define the migration strategy from version 2 to version 3
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `remote_keys` (
                        `id` TEXT NOT NULL,
                        `prevKey` INTEGER,
                        `nextKey` INTEGER,
                        PRIMARY KEY(`id`)
                    )
                """)
            }
        }
    }
}
