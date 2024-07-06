package com.esnanta.storyapp.ui.story

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.esnanta.storyapp.data.source.remote.response.ListStoryItem
import com.esnanta.storyapp.databinding.ActivityListStoryBinding

class ListStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListStoryBinding
    private lateinit var adapter: StoryAdapter
    private lateinit var stories: List<ListStoryItem> // This should be populated with your data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // Assuming stories is populated from your API call
        adapter = StoryAdapter(stories)
        binding.recyclerView.adapter = adapter
    }
}