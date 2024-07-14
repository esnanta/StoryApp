package com.esnanta.storyapp.ui.base

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.esnanta.storyapp.R
import com.esnanta.storyapp.ui.login.LoginActivity
import com.esnanta.storyapp.ui.main.MainActivity
import com.esnanta.storyapp.ui.main.MainViewModel
import com.esnanta.storyapp.ui.welcome.WelcomeActivity
import com.esnanta.storyapp.utils.factory.UserViewModelFactory

abstract class BaseActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> {
        UserViewModelFactory.getInstance(this)
    }

    private var isUserLoggedIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.getSession().observe(this) { user ->
            if (isUserLoggedIn != user.isLogin) {
                isUserLoggedIn = user.isLogin
                invalidateOptionsMenu() // Request to refresh the menu
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        updateMenuItems(menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        updateMenuItems(menu)
        return super.onPrepareOptionsMenu(menu)
    }

    private fun updateMenuItems(menu: Menu?) {
        menu?.let {
            val homeMenuItem = it.findItem(R.id.action_home)
            val loginLogoutMenuItem = it.findItem(R.id.action_login_logout)
            if (isUserLoggedIn) {
                loginLogoutMenuItem.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_logout_24)
                loginLogoutMenuItem.title = getString(R.string.menu_logout)
            } else {
                homeMenuItem.setVisible(false)
                loginLogoutMenuItem.setVisible(false)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_home -> {
                goHome()
                true
            }
            R.id.action_setting -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            R.id.action_login_logout -> {
                if (isUserLoggedIn) {
                    showLogoutConfirmationDialog()
                } else {
                    startActivity(Intent(this, LoginActivity::class.java))
                }
                true
            }
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun goHome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(this, R.style.AlertDialogCustom)
            .setTitle(R.string.logout_confirmation_title)
            .setMessage(R.string.logout_confirmation_message)
            .setPositiveButton(R.string.yes) { dialog, _ ->
                viewModel.logout()
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
                dialog.dismiss()
            }
            .setNegativeButton(R.string.no) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    fun showLoginSuccessDialog() {
        AlertDialog.Builder(this, R.style.AlertDialogCustom).apply {
            setTitle(getString(R.string.login_success_title))
            setMessage(getString(R.string.login_success_message))
            setPositiveButton(getString(R.string.login_success_positive_button)) { _, _ ->
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            create()
            show()
        }
    }
}