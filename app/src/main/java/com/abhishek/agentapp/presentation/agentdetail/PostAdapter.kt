package com.abhishek.agentapp.presentation.agentdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abhishek.agentapp.databinding.ItemPostBinding
import com.abhishek.agentapp.domain.model.AgentPost

class PostAdapter : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    private val items = mutableListOf<AgentPost>()

    inner class PostViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(post: AgentPost) {
            binding.apply {
                tvPostTitle.text = post.title
                tvPostBody.text = post.body
                tvPostLikes.text = "${post.likes} likes"
                tvPostViews.text = "${post.views} views"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun submitList(newItems: List<AgentPost>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}