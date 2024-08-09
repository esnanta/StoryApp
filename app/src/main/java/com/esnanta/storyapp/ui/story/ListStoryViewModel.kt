package com.esnanta.storyapp.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.esnanta.storyapp.data.repository.StoryRepository
import com.esnanta.storyapp.data.source.remote.response.ListStoryItem

class ListStoryViewModel(repository: StoryRepository) : ViewModel() {

    val listStory: LiveData<PagingData<ListStoryItem>> = repository.getListStory().cachedIn(viewModelScope).asLiveData()

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _dialogMessage = MutableLiveData<String?>()
    val dialogMessage: LiveData<String?> = _dialogMessage

    private val _loadState = MutableLiveData<CombinedLoadStates>()
    val loadState: LiveData<CombinedLoadStates> = _loadState

    fun observeLoadState(loadStates: CombinedLoadStates) {
        _loadState.postValue(loadStates)

        val errorState = loadStates.source.append as? LoadState.Error
            ?: loadStates.source.prepend as? LoadState.Error
            ?: loadStates.append as? LoadState.Error
            ?: loadStates.prepend as? LoadState.Error
        errorState?.let {
            _dialogMessage.postValue(it.error.localizedMessage)
        }
    }

    fun clearDialogMessage() {
        _dialogMessage.value = null
    }

    fun refreshStories(adapter: ListStoryAdapter) {
        adapter.refresh()
    }
}