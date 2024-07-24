package com.esnanta.storyapp.ui.story

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esnanta.storyapp.data.repository.StoryRepository
import com.esnanta.storyapp.data.source.remote.Result
import com.esnanta.storyapp.data.source.remote.response.Story
import com.esnanta.storyapp.data.source.remote.response.StoryResponse
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StoryMapViewModel(private val repository: StoryRepository) : ViewModel() {

    private val _storiesWithLocation = MutableLiveData<Result<StoryResponse>>()
    val storiesWithLocation: LiveData<Result<StoryResponse>> get() = _storiesWithLocation

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
                _storiesWithLocation.postValue(Result.Error(e.message ?: "Unknown error"))
                _dialogMessage.postValue(e.message ?: "Unknown error")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun clearDialogMessage() {
        _dialogMessage.value = null
    }

    fun addMarkers(stories: List<Story>?, googleMap: GoogleMap) {
        val defaultLatLng = LatLng(-6.200000, 106.816666) // Jakarta, Indonesia
        val defaultZoomLevel = 10f
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (stories.isNullOrEmpty()) {
                    // If stories list is empty, move camera to the default location
                    Log.d("StoryMapActivity", "No stories found. Moving camera to default location.")
                    withContext(Dispatchers.Main) {
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLatLng, defaultZoomLevel))
                    }
                } else {
                    // Add markers and move camera to the first story's location
                    Log.d("StoryMapActivity", "Adding markers for ${stories.size} stories.")
                    stories.forEach { story ->
                        Log.d("StoryMapActivity", "Adding marker for story: $story")
                        val lat = story.lat
                        val lon = story.lon
                        if (lat != null && lon != null) {
                            val latLng = LatLng(lat, lon)
                            withContext(Dispatchers.Main) {
                                googleMap.addMarker(
                                    MarkerOptions()
                                        .position(latLng)
                                        .title(story.name)
                                        .snippet(story.description)
                                )
                            }
                        }
                    }
                    // Move the camera to the first story's location
                    stories.firstOrNull { it.lat != null && it.lon != null }?.let { story ->
                        val firstLocation = LatLng(story.lat!!, story.lon!!)
                        withContext(Dispatchers.Main) {
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 10f))
                        }
                    }
                }
            }
        }
    }
}