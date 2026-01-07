package com.abhishek.agentapp.presentation.agentlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhishek.agentapp.domain.model.Agent
import com.abhishek.agentapp.domain.repository.AgentRepository
import com.abhishek.agentapp.domain.usecase.AgentUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AgentListViewModel @Inject constructor(
    private val useCases: AgentUseCases
) : ViewModel() {

    private val _agents = MutableStateFlow<List<Agent>>(emptyList())
    val agents: StateFlow<List<Agent>> = _agents

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private var offset = 0
    private var searchQuery = ""
    private var searchJob: Job? = null

    init {
        loadAgents()
    }

    fun loadAgents() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            offset = 0
            val result = useCases.getAgents(offset = 0)
            result.onSuccess { agents ->
                _agents.value = agents
                offset = agents.size
            }.onFailure {
                _error.value = it.message ?: "Unknown error"
            }
            _isLoading.value = false
        }
    }

    fun loadMore() {
        viewModelScope.launch {
            val result = useCases.getAgents(offset = offset)
            result.onSuccess { newAgents ->
                _agents.value += newAgents
                offset += newAgents.size
            }
        }
    }

    fun search(query: String) {
        searchQuery = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300) // Debounce
            _isLoading.value = true
            _error.value = null

            val result = if (query.isEmpty()) {
                useCases.getAgents(offset = 0)
            } else {
                useCases.searchAgents(query)
            }

            result.onSuccess { agents ->
                _agents.value = agents
            }.onFailure {
                _error.value = it.message ?: "Search failed"
            }
            _isLoading.value = false
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _isLoading.value = true
            useCases.refreshAgents()
                .onSuccess { agents ->
                    _agents.value = agents
                    offset = agents.size
                }
                .onFailure {
                    _error.value = it.message
                }
            _isLoading.value = false
        }
    }
}