package com.esnanta.storyapp.ui.story

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.esnanta.storyapp.R
import com.esnanta.storyapp.data.source.remote.Result
import com.esnanta.storyapp.databinding.ActivityAddStoryBinding
import com.esnanta.storyapp.ui.base.BaseActivity
import com.esnanta.storyapp.utils.factory.StoryViewModelFactory
import com.esnanta.storyapp.utils.widgets.getImageUri
import com.esnanta.storyapp.utils.widgets.reduceFileImage
import com.esnanta.storyapp.utils.widgets.uriToFile
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class AddStoryActivity : BaseActivity() {

    private val viewModel by viewModels<AddStoryViewModel> {
        StoryViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentImageUri: Uri? = null
    private var currentLocation: Location? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[PERMISSION_FINE_LOCATION] ?: false
        val coarseLocationGranted = permissions[PERMISSION_COARSE_LOCATION] ?: false

        if (fineLocationGranted || coarseLocationGranted) {
            getLastLocation()
        } else {
            showToast("Location permission denied")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.uploadButton.setOnClickListener { uploadImage() }
        binding.useLocation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (checkGooglePlayServices() && checkLocationServices()) {
                    checkLocationPermission()
                } else {
                    showToast(getString(R.string.please_enable_google_play_and_location_service))
                    binding.useLocation.isChecked = false
                }
            }
        }

        observeViewModel()
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                PERMISSION_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(
                arrayOf(
                    PERMISSION_FINE_LOCATION,
                    PERMISSION_COARSE_LOCATION
                )
            )
        } else {
            getLastLocation()
        }
    }

    private fun checkGooglePlayServices(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = apiAvailability.isGooglePlayServicesAvailable(this)
        return resultCode == ConnectionResult.SUCCESS
    }

    private fun checkLocationServices(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkLocationServices()) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        currentLocation = location
                    } else {
                        showToast(getString(R.string.location_not_available))
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("Location Error", e.message ?: "Unknown error")
                    showToast(getString(R.string.failed_to_get_location))
                }
        } else {
            showToast(getString(R.string.please_enable_google_play_and_location_service))
        }
    }

    private fun startGallery() {
        launcherGallery.launch(
            PickVisualMediaRequest(
                ActivityResultContracts.PickVisualMedia.ImageOnly
            )
        )
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            val description = binding.descriptionEditText.text.toString()
            if (description.isEmpty()) {
                showToast(getString(R.string.empty_image_warning))
                return
            }

            if (binding.useLocation.isChecked) {
                currentLocation?.let { location ->
                    viewModel.uploadImage(imageFile, description, location.latitude, location.longitude)
                } ?: showToast(getString(R.string.location_not_available))
            } else {
                viewModel.uploadImage(imageFile, description, null, null)
            }
        } ?: showToast(getString(R.string.empty_image_warning))
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.uploadResult.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    // Handled by isLoading LiveData
                }
                is Result.Success -> {
                    showToast(result.data.message ?: getString(R.string.upload_success))
                    viewModel.clearDialogMessage()
                    navigateToListStory()
                }
                is Result.Error -> {
                    showToast(result.error)
                    viewModel.clearDialogMessage()
                }
                else -> {
                    showToast(getString(R.string.unknown_error))
                    viewModel.clearDialogMessage()
                }
            }
        }

        viewModel.dialogMessage.observe(this) { message ->
            message?.let {
                showToast(it)
                viewModel.clearDialogMessage()
            }
        }
    }

    private fun navigateToListStory() {
        val intent = Intent(this, ListStoryActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    companion object {
        private const val PERMISSION_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
        private const val PERMISSION_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
    }
}
