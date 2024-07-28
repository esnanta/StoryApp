package com.esnanta.storyapp.ui.story

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.esnanta.storyapp.databinding.ActivityListStoryBinding
import com.esnanta.storyapp.ui.base.BaseActivity
import com.esnanta.storyapp.utils.factory.StoryViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ListStoryActivity : BaseActivity() {
    private val viewModel by viewModels<ListStoryViewModel> {
        StoryViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityListStoryBinding
    private lateinit var storyAdapter: ListStoryAdapter
    private lateinit var loadStateAdapter: LoadingStateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.fabAddStory.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }

        setupRecyclerView()
        setupSwipeRefresh()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        storyAdapter = ListStoryAdapter()
        loadStateAdapter = LoadingStateAdapter { storyAdapter.retry() }

        val concatAdapter = storyAdapter.withLoadStateFooter(loadStateAdapter)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = concatAdapter
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            storyAdapter.refresh()
        }
    }

    private fun observeViewModel() {
        viewModel.listStory.observe(this) { pagingData ->
            storyAdapter.submitData(lifecycle, pagingData)
        }

        lifecycleScope.launch {
            storyAdapter.loadStateFlow.collectLatest { loadStates ->
                binding.swipeRefreshLayout.isRefreshing = loadStates.refresh is LoadState.Loading

                // Show retry button and error message on the screen when there's an error
                val errorState = loadStates.source.append as? LoadState.Error
                    ?: loadStates.source.prepend as? LoadState.Error
                    ?: loadStates.append as? LoadState.Error
                    ?: loadStates.prepend as? LoadState.Error
                errorState?.let {
                    Toast.makeText(this@ListStoryActivity, it.error.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.dialogMessage.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                viewModel.clearDialogMessage()
            }
        }
    }
}