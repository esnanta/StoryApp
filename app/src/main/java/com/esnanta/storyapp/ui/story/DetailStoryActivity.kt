package com.esnanta.storyapp.ui.story

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.esnanta.storyapp.data.source.remote.Result
import com.esnanta.storyapp.databinding.ActivityDetailStoryBinding
import com.esnanta.storyapp.ui.base.BaseActivity
import com.esnanta.storyapp.utils.factory.StoryViewModelFactory
import com.esnanta.storyapp.utils.widgets.DateFormatter
import com.esnanta.storyapp.utils.widgets.loadImage
import java.util.TimeZone

class DetailStoryActivity : BaseActivity() {
    private val viewModel by viewModels<DetailStoryViewModel> {
        StoryViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.fabAddStory.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        val storyId = intent.getStringExtra(EXTRA_STORY_ID)
        if (storyId != null) {
            viewModel.fetchStoryDetail(storyId)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.storyDetail.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    // Handled by isLoading LiveData
                }
                is Result.Success -> {
                    val detailStoryItem = result.data.story
                    binding.tvName.text = detailStoryItem?.name
                    binding.tvDescription.text = detailStoryItem?.description
                    binding.tvCreatedAt.text =
                        DateFormatter.formatDate(
                            detailStoryItem?.createdAt.toString(), TimeZone.getDefault().id
                        )

                    detailStoryItem?.photoUrl?.let {
                        binding.ivPhoto.loadImage(
                            url = it
                        )
                    }

                }
                is Result.Error -> {
                    // Show error message
                    showToast(result.error)
                }
            }
        }

        viewModel.dialogMessage.observe(this) { message ->
            message?.let {
                showToast(it)
                viewModel.clearDialogMessage()
            }
        }
    }

    companion object {
        const val EXTRA_STORY_ID = "extra_story_id"
    }
}