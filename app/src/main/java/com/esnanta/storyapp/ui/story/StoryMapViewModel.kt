package com.esnanta.storyapp.ui.story

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esnanta.storyapp.R
import com.esnanta.storyapp.data.repository.StoryRepository
import com.esnanta.storyapp.data.source.remote.Result
import com.esnanta.storyapp.data.source.remote.response.ListStoryItem
import com.esnanta.storyapp.data.source.remote.response.ListStoryResponse
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StoryMapViewModel(private val repository: StoryRepository) : ViewModel() {

    private val _storiesWithLocation = MutableLiveData<Result<ListStoryResponse>>()
    val storiesWithLocation: LiveData<Result<ListStoryResponse>> get() = _storiesWithLocation

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _dialogMessage = MutableLiveData<String?>()
    val dialogMessage: LiveData<String?> get() = _dialogMessage

    fun fetchStoriesWithLocation() {
        viewModelScope.launch {
            _isLoading.value = true
            _storiesWithLocation.postValue(Result.Loading)
            try {
                val result = withContext(Dispatchers.IO) {
                    repository.getStoriesWithLocation()
                }
                _storiesWithLocation.postValue(result)
                if (result is Result.Success) {
                    _dialogMessage.postValue(result.data.message)
                } else if (result is Result.Error) {
                    _dialogMessage.postValue(result.error)
                }
            } catch (e: Exception) {
                _storiesWithLocation.postValue(
                    Result.Error(
                        e.message ?: R.string.unknown_error.toString()
                    )
                )
                _dialogMessage.postValue(e.message ?: R.string.unknown_error.toString())
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun clearDialogMessage() {
        _dialogMessage.value = null
    }

    fun addMarkers(detailStoryItem: List<ListStoryItem>?, googleMap: GoogleMap) {
        viewModelScope.launch(Dispatchers.Default) {
            if (detailStoryItem.isNullOrEmpty()) {
                withContext(Dispatchers.Main) {
                    val defaultLatLng = LatLng(-6.200000, 106.816666) // Jakarta, Indonesia
                    val defaultZoomLevel = 10f
                    googleMap.minZoomLevel
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLatLng, defaultZoomLevel))
                }
            } else {
                val markers = detailStoryItem.mapNotNull { story ->
                    val lat = story.lat
                    val lon = story.lon
                    if (lat != null && lon != null) {
                        MarkerOptions()
                            .position(LatLng(lat, lon))
                            .title(story.name)
                            .snippet(story.description)
                    } else {
                        null
                    }
                }

                withContext(Dispatchers.Main) {
                    markers.forEach { markerOptions ->
                        googleMap.addMarker(markerOptions)
                    }

                    detailStoryItem.firstOrNull { it.lat != null && it.lon != null }?.let { story ->
                        val firstLocation = LatLng(story.lat!!, story.lon!!)
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 10f))
                    }
                }
            }
        }
    }
}