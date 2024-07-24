package com.esnanta.storyapp.ui.story

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.esnanta.storyapp.R
import com.esnanta.storyapp.data.source.remote.Result
import com.esnanta.storyapp.databinding.ActivityStoryMapBinding
import com.esnanta.storyapp.ui.base.BaseActivity
import com.esnanta.storyapp.utils.factory.StoryViewModelFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

class StoryMapActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityStoryMapBinding
    private val viewModel by viewModels<StoryMapViewModel> {
        StoryViewModelFactory.getInstance(this)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStoryMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

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
        getMyLocation()
    }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun setupObserver() {
        viewModel.storiesWithLocation.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    // Handled by observer
                    Log.d("StoryMapActivity", "Loading stories with location...")
                }
                is Result.Success -> {
                    Log.d("StoryMapActivity", "Successfully loaded stories with location.")
                    result.data?.let {
                        viewModel.addMarkers(it.listStory, mMap)
                    }
                }
                is Result.Error -> {
                    Log.e("StoryMapActivity", "Error loading stories: ${result.error}")
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.dialogMessage.observe(this) { message ->
            message?.let {
                showDialog(message)
                viewModel.clearDialogMessage()
            }
        }
    }

    private fun showDialog(message: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }
}
