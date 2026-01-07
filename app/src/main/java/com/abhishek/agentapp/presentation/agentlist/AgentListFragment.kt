package com.abhishek.agentapp.presentation.agentlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abhishek.agentapp.databinding.FragmentAgentListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AgentListFragment : Fragment() {

    private lateinit var binding: FragmentAgentListBinding
    private val viewModel: AgentListViewModel by viewModels()
    private lateinit var adapter: AgentAdapter
    private var isLoadingMore = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAgentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        adapter = AgentAdapter { agent ->
            val action = AgentListFragmentDirections.actionListToDetail(agent.id)
            findNavController().navigate(action)
        }

        binding.recyclerViewAgents.apply {
            adapter = this@AgentListFragment.adapter
            layoutManager = LinearLayoutManager(context)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    if (layoutManager.findLastVisibleItemPosition() >=
                        adapter!!.itemCount - 5 && !isLoadingMore) {
                        isLoadingMore = true
                        viewModel.loadMore()
                    }
                }
            })
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }

        binding.etSearch.addTextChangedListener { text ->
            viewModel.search(text.toString())
        }

        binding.buttonSettings.setOnClickListener {
            val action = AgentListFragmentDirections.actionListToSettings()
            findNavController().navigate(action)
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.agents.collect { agents ->
                        adapter.submitList(agents)
                        isLoadingMore = false
                    }
                }
                launch {
                    viewModel.isLoading.collect { isLoading ->
                        binding.swipeRefresh.isRefreshing = isLoading
                    }
                }
                launch {
                    viewModel.error.collect { error ->
                        error?.let {
                            showError(it)
                        }
                    }
                }
            }
        }
    }

    private fun showError(message: String) {
        // Show snackbar or toast
        android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show()
    }
}