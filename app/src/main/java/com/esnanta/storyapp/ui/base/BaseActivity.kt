package com.esnanta.storyapp.ui.base

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.esnanta.storyapp.R
import com.esnanta.storyapp.ui.login.LoginActivity
import com.esnanta.storyapp.utils.factory.StoryViewModelFactory
import com.esnanta.storyapp.ui.main.MainActivity
import com.esnanta.storyapp.ui.main.MainViewModel
import com.esnanta.storyapp.ui.story.ListStoryViewModel
import com.esnanta.storyapp.ui.welcome.WelcomeActivity
import com.esnanta.storyapp.utils.factory.UserViewModelFactory

abstract class BaseActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> {
        UserViewModelFactory.getInstance(this)
    }

    private var isUserLoggedIn = false

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        // Observe the session status
        viewModel.getSession().observe(this) { user ->
            isUserLoggedIn = user.isLogin
            invalidateOptionsMenu() // Request to refresh the menu
        }

        // Update the menu items based on the login status
        menu?.let {
            val loginLogoutMenuItem = it.findItem(R.id.action_login_logout)
            if (isUserLoggedIn) {
                loginLogoutMenuItem.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_logout_24)
                loginLogoutMenuItem.title = getString(R.string.menu_logout)
            } else {
                loginLogoutMenuItem.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_login_24)
                loginLogoutMenuItem.title = getString(R.string.menu_login)
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_home -> {
                goHome()
                true
            }
            R.id.action_login_logout -> {
                if (isUserLoggedIn) {
                    viewModel.showLogoutConfirmationDialog(this) {
                        goHome()
                    }
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

    private fun goHome(){
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}