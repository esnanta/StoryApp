package com.esnanta.storyapp.ui.story

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.esnanta.storyapp.R
import com.esnanta.storyapp.data.source.remote.Result
import com.esnanta.storyapp.databinding.ActivityListStoryBinding
import com.esnanta.storyapp.di.StoryViewModelFactory

class ListStoryActivity : AppCompatActivity() {
    private val viewModel by viewModels<ListStoryViewModel> {
        StoryViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityListStoryBinding
    private lateinit var adapter: ListStoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        setupRecyclerView()
        observeViewModel()
        viewModel.fetchListStory()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_home -> {
                // Handle home action
                true
            }
            R.id.action_log_out -> {
                // Handle log out action
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupRecyclerView() {
        adapter = ListStoryAdapter(emptyList())
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun observeViewModel() {

        viewModel.listStory.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    // Handled by isLoading LiveData
                }
                is Result.Success -> {
                    adapter = ListStoryAdapter(result.data)
                    binding.recyclerView.adapter = adapter
                }
                is Result.Error -> {
                    // Show error message
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.dialogMessage.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                viewModel.clearDialogMessage()
            }
        }
    }
}