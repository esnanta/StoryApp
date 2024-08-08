package com.esnanta.storyapp.ui.story

import android.content.Intent
import android.os.Bundle
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
        observeLoadState()
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
            viewModel.refreshStories(storyAdapter)
        }
    }

    private fun observeViewModel() {
        viewModel.listStory.observe(this) { pagingData ->
            storyAdapter.submitData(lifecycle, pagingData)
        }

        viewModel.loadState.observe(this) { loadStates ->
            binding.swipeRefreshLayout.isRefreshing = loadStates.refresh is LoadState.Loading
        }

        viewModel.dialogMessage.observe(this) { message ->
            message?.let {
                showToast(it)
                viewModel.clearDialogMessage()
            }
        }
    }

    private fun observeLoadState() {
        lifecycleScope.launch {
            storyAdapter.loadStateFlow.collectLatest { loadStates ->
                viewModel.observeLoadState(loadStates)
            }
        }
    }
}