package com.dicoding.storyapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.ItemLoadingBinding

class LoadingStoryAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<LoadingStoryAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View, retry: () -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemLoadingBinding.bind(itemView)
        init {
            binding.btnRefresh.setOnClickListener {
                binding.btnRefresh.isVisible = false
                binding.tvErrorMessage.isVisible = false
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.tvErrorMessage.text = loadState.error.localizedMessage
            }
            binding.pbLoadingScreen.isVisible = loadState is LoadState.Loading
            binding.btnRefresh.isVisible = loadState is LoadState.Error
            binding.tvErrorMessage.isVisible = loadState is LoadState.Error
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false),
            retry
        )
    }
}