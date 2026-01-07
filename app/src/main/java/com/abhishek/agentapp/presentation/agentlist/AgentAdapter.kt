package com.abhishek.agentapp.presentation.agentlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.abhishek.agentapp.databinding.ItemAgentBinding
import com.abhishek.agentapp.domain.model.Agent
import com.bumptech.glide.Glide

class AgentAdapter(
    private val onAgentClick: (Agent) -> Unit
) : RecyclerView.Adapter<AgentAdapter.AgentViewHolder>() {

    private val items = mutableListOf<Agent>()

    inner class AgentViewHolder(private val binding: ItemAgentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(agent: Agent) {
            binding.apply {
                tvAgentName.text = agent.fullName
                tvAgentEmail.text = agent.email
                tvAgentCompany.text = agent.company
                tvAgentPhone.text = agent.phone

                Glide.with(ivAgentImage)
                    .load(agent.image)
                    .circleCrop()
                    .into(ivAgentImage)

                root.setOnClickListener {
                    onAgentClick(agent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgentViewHolder {
        val binding = ItemAgentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AgentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AgentViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun submitList(newItems: List<Agent>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize() = items.size
            override fun getNewListSize() = newItems.size
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                items[oldItemPosition].id == newItems[newItemPosition].id
            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                items[oldItemPosition] == newItems[newItemPosition]
        }
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items.clear()
        items.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

    fun appendList(newItems: List<Agent>) {
        val startPosition = items.size
        items.addAll(newItems)
        notifyItemRangeInserted(startPosition, newItems.size)
    }
}
