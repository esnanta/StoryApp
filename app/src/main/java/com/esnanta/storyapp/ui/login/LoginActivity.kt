package com.esnanta.storyapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.esnanta.storyapp.data.model.UserModel
import com.esnanta.storyapp.databinding.ActivityLoginBinding
import com.esnanta.storyapp.di.ViewModelFactory
import com.esnanta.storyapp.ui.main.MainActivity
import kotlinx.coroutines.flow.Flow


class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()

        viewModel.userSession.observe(this) { user ->
            user?.let {
                if (it.email.isNotEmpty()) {
                    binding.emailEditText.setText(it.email)
                }
                if (it.password.isNotEmpty()) {
                    binding.passwordEditText.setText(it.password)
                }
            }
        }
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
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (viewModel.checkCredentials(email, password)) {
                val currentUser = viewModel.getCurrentUser()
                val token = "sample_token"
                currentUser?.let { user ->
                    viewModel.saveSession(UserModel(user.name, email, password, token))
                }
                showLoginSuccessDialog()
            } else {
                showConfirmationDialog(email, password)
            }
        }
    }

    private fun showLoginSuccessDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("Yeah!")
            setMessage("Anda berhasil login. Sudah tidak sabar untuk belajar ya?")
            setPositiveButton("Lanjut") { _, _ ->
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            create()
            show()
        }
    }

    private fun showConfirmationDialog(email: String, password: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Warning")
            setMessage("The email or password you entered is different from the stored data. Do you want to update the stored data?")
            setPositiveButton("Yes") { _, _ ->
                val currentUser = viewModel.getCurrentUser()
                val token = "sample_token"
                currentUser?.let { user ->
                    viewModel.saveSession(UserModel(user.name, email, password, token))
                }
                showLoginSuccessDialog()
            }
            setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            create()
            show()
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val message = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)
        val emailTextView = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                message,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                login
            )
            startDelay = 100
        }.start()
    }
}
