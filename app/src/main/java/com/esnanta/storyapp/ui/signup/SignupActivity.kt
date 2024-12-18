package com.esnanta.storyapp.ui.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.esnanta.storyapp.R
import com.esnanta.storyapp.data.source.remote.Result
import com.esnanta.storyapp.databinding.ActivitySignupBinding
import com.esnanta.storyapp.ui.base.BaseActivity
import com.esnanta.storyapp.utils.factory.UserViewModelFactory

class SignupActivity : BaseActivity() {
    private lateinit var binding: ActivitySignupBinding

    private val viewModel by viewModels<SignupViewModel> {
        UserViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        setupView()
        setupAction()
        setupObserver()
        playAnimation()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        //supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            viewModel.register(name, email, password)
        }
    }

    private fun setupObserver() {
        viewModel.registerResult.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    // Loading handled by isLoading observer
                }
                is Result.Success -> {
                    val name = binding.nameEditText.text.toString()
                    val email = binding.emailEditText.text.toString()
                    val successMessage = getString(R.string.registration_success_message, result.data.message, name, email)
                    viewModel.onRegistrationSuccess(successMessage)
                }
                is Result.Error -> {
                    viewModel.onRegistrationError(result.error)
                }
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.dialogMessage.observe(this) { message ->
            message?.let {
                AlertDialog.Builder(this).apply {
                    setTitle(if (viewModel.registerResult.value is Result.Error) "Error" else "Yeah!")
                    setMessage(it)
                    setPositiveButton("OK") { _, _ ->
                        if (viewModel.registerResult.value is Result.Success) finish()
                    }
                    create()
                    show()
                }
                viewModel.clearDialogMessage()
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val nameTextView =
            ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)


        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                signup
            )
            startDelay = 100
        }.start()
    }
}