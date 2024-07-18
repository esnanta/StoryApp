package com.esnanta.storyapp.ui.story

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.esnanta.storyapp.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.esnanta.storyapp.databinding.ActivityStoryMapBinding
import com.esnanta.storyapp.ui.base.BaseActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StoryMapActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityStoryMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStoryMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        // Load the map asynchronously
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Load map data asynchronously
        loadMapData()
    }

    private fun loadMapData() {
        CoroutineScope(Dispatchers.IO).launch {
            // Simulate network or database call to load map data
            val location = LatLng(-34.0, 151.0)
            withContext(Dispatchers.Main) {
                mMap.addMarker(MarkerOptions().position(location).title("Marker in Sydney"))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
            }
        }
    }
}
