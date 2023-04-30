package com.dicoding.storyapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.remote.response.StoryItem
import com.dicoding.storyapp.databinding.StoryItemBinding
import com.dicoding.storyapp.view.detail.DetailStoryActivity

class StoryAdapter(
    private val listStory: List<StoryItem>
) : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = StoryItemBinding.bind(itemView)
        fun bindItem(item: StoryItem) {
            with(binding) {
                Glide.with(itemView)
                    .load(item.photoUrl)
                    .into(ivItemPhoto)

                tvItemName.text = item.name
                tvItemDescription.text = item.description

                itemView.setOnClickListener {
                    DetailStoryActivity.start(
                        itemView.context,
                        item.photoUrl as String,
                        item.id as String,
                        Pair(ivItemPhoto, "ivItemPhoto")
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.story_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(listStory[position])
    }

    override fun getItemCount(): Int = listStory.size
}