package com.esnanta.storyapp.ui.story

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.esnanta.storyapp.R
import com.esnanta.storyapp.data.source.remote.response.ListStoryItem
import com.esnanta.storyapp.databinding.ItemStoryBinding

class ListStoryAdapter :
    PagingDataAdapter<ListStoryItem, ListStoryAdapter.StoryViewHolder>(StoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)
            holder.itemView.animation =
                AnimationUtils.loadAnimation(holder.itemView.context, R.anim.property_style_one)
        }
    }

    inner class StoryViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(story: ListStoryItem) {
            binding.tvName.text = story.name
            binding.tvDescription.text = story.description
            binding.tvCreatedAt.text = story.createdAt

            Glide.with(binding.root.context)
                .load(story.photoUrl)
                .into(binding.ivPhoto)

            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, DetailStoryActivity::class.java)
                intent.putExtra(DetailStoryActivity.EXTRA_STORY_ID, story.id)
                context.startActivity(intent)
            }
        }
    }
}