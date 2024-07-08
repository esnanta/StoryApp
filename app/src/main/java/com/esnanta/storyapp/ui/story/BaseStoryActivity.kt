package com.esnanta.storyapp.ui.story

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.esnanta.storyapp.R

abstract class BaseStoryActivity : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_home -> {
                // Handle home action
                true
            }
            R.id.action_log_out -> {
                // Handle log out action
                true
            }
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}