package com.esnanta.storyapp.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.esnanta.storyapp.data.repository.StoryRepository
import com.esnanta.storyapp.ui.story.ListStoryViewModel
import com.esnanta.storyapp.ui.story.DetailStoryViewModel

class StoryViewModelFactory(private val repository: StoryRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ListStoryViewModel::class.java) -> {
                ListStoryViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailStoryViewModel::class.java) -> {
                DetailStoryViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: StoryViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): StoryViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: StoryViewModelFactory(
                    Injection.provideStoryRepository(context)
                )
            }.also { INSTANCE = it }
        }
    }
}