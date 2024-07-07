package com.esnanta.storyapp.ui.story

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.esnanta.storyapp.data.source.remote.Result
import com.esnanta.storyapp.databinding.ActivityListStoryBinding
import com.esnanta.storyapp.di.StoryViewModelFactory

class ListStoryActivity : AppCompatActivity() {
    private val viewModel by viewModels<ListStoryViewModel> {
        StoryViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityListStoryBinding
    private lateinit var adapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = StoryAdapter(emptyList())
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.getListStory().observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    // Show loading indicator
                }
                is Result.Success -> {
                    adapter = StoryAdapter(result.data.listStory)
                    binding.recyclerView.adapter = adapter
                }
                is Result.Error -> {
                    // Show error message
                }
            }
        }
    }
}