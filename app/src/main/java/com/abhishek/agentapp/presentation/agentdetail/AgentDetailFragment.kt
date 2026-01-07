package com.abhishek.agentapp.presentation.agentdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.abhishek.agentapp.databinding.FragmentAgentDetailBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AgentDetailFragment : Fragment() {

    private lateinit var binding: FragmentAgentDetailBinding
    private val viewModel: AgentDetailViewModel by viewModels()
    private lateinit var postAdapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAgentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        val agentId = arguments?.getInt("agent_id") ?: 0
        viewModel.agentDetails(agentId)
        postAdapter = PostAdapter()
        binding.recyclerViewPosts.apply {
            adapter = postAdapter
            layoutManager = LinearLayoutManager(context)
        }

    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->

                    binding.shimmerContainer.visibility =
                        if (state.isLoading) View.VISIBLE else View.GONE

                    // Error (optional)
                    state.error?.let {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    }

                    // Agent
                    state.agent?.let { agent ->
                        binding.tvAgentName.text = agent.fullName
                        binding.tvAgentEmail.text = agent.email
                        binding.tvAgentCompany.text = agent.company
                        binding.tvAgentPhone.text = agent.phone

                        Glide.with(binding.ivAgentImage)
                            .load(agent.image)
                            .into(binding.ivAgentImage)
                    }

                    // Posts
                    postAdapter.submitList(state.posts)
                }
            }
        }
    }
}