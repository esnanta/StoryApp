package com.esnanta.storyapp.ui.storydetail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.esnanta.storyapp.data.source.remote.Result
import com.esnanta.storyapp.databinding.ActivityDetailStoryBinding
import com.esnanta.storyapp.di.StoryViewModelFactory

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding
    private val viewModel by viewModels<DetailStoryViewModel> {
        StoryViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyId = intent.getStringExtra(EXTRA_STORY_ID)
        if (storyId != null) {
            observeViewModel(storyId)
        }
    }

    private fun observeViewModel(storyId: String) {
        viewModel.getStoryDetail(storyId).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    // Show loading indicator
                }
                is Result.Success -> {
                    val story = result.data.story
                    binding.tvName.text = story?.name
                    binding.tvDescription.text = story?.description
                    binding.tvCreatedAt.text = story?.createdAt

                    Glide.with(this)
                        .load(story?.photoUrl)
                        .into(binding.ivPhoto)
                }
                is Result.Error -> {
                    // Show error message
                }
            }
        }
    }

    companion object {
        const val EXTRA_STORY_ID = "extra_story_id"
    }
}