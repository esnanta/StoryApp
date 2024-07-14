package com.esnanta.storyapp.ui.story

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.esnanta.storyapp.R
import com.esnanta.storyapp.data.source.remote.Result
import com.esnanta.storyapp.databinding.ActivityAddStoryBinding
import com.esnanta.storyapp.ui.base.BaseActivity
import com.esnanta.storyapp.utils.factory.AddStoryViewModelFactory
import com.esnanta.storyapp.utils.widgets.getImageUri
import com.esnanta.storyapp.utils.widgets.reduceFileImage
import com.esnanta.storyapp.utils.widgets.uriToFile

class AddStoryActivity : BaseActivity() {

    private val viewModel by viewModels<AddStoryViewModel> {
        AddStoryViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityAddStoryBinding
    private var currentImageUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.uploadButton.setOnClickListener { uploadImage() }

        observeViewModel()
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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

            viewModel.uploadImage(imageFile, description)
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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToListStory() {
        val intent = Intent(this, ListStoryActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}