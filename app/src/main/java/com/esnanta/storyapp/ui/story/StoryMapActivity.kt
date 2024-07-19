package com.esnanta.storyapp.ui.story

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.esnanta.storyapp.R
import com.esnanta.storyapp.data.source.remote.Result
import com.esnanta.storyapp.data.source.remote.response.Story
import com.esnanta.storyapp.databinding.ActivityStoryMapBinding
import com.esnanta.storyapp.ui.base.BaseActivity
import com.esnanta.storyapp.utils.factory.StoryViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class StoryMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityStoryMapBinding
    private lateinit var viewModel: StoryMapViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStoryMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setSupportActionBar(binding.toolbar)

        setupViewModel()
        setupObserver()

        // Load the map asynchronously
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Load map data asynchronously
        viewModel.fetchStoriesWithLocation()
    }

    private fun setupViewModel() {
        val factory = StoryViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[StoryMapViewModel::class.java]
    }

    private fun setupObserver() {
        viewModel.storiesWithLocation.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    // Show loading indicator if needed
                }
                is Result.Success -> {
                    // Handle successful response and add markers
                    addMarkers(result.data.listStory)
                }
                is Result.Error -> {
                    // Handle error response
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun addMarkers(stories: List<Story>?) {
        stories?.forEach { story ->
            val location = LatLng(story.lat ?: 0.0, story.lon ?: 0.0)
            mMap.addMarker(MarkerOptions().position(location).title(story.name))
        }
        // Optionally move camera to the first story location
        stories?.firstOrNull()?.let { story ->
            val firstLocation = LatLng(story.lat ?: 0.0, story.lon ?: 0.0)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(firstLocation))
        }
    }
}
