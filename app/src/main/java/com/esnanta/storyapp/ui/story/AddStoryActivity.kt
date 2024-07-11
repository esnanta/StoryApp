package com.esnanta.storyapp.ui.story

import android.os.Bundle
import com.esnanta.storyapp.databinding.ActivityAddStoryBinding
import com.esnanta.storyapp.ui.base.BaseActivity

class AddStoryActivity : BaseActivity() {

    private lateinit var binding: ActivityAddStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
    }
}