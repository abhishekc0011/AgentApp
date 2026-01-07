package com.abhishek.agentapp.presentation.agentdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhishek.agentapp.domain.model.Agent
import com.abhishek.agentapp.domain.model.AgentPost
import com.abhishek.agentapp.domain.repository.AgentRepository
import com.abhishek.agentapp.domain.usecase.AgentUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AgentDetailUiState(
    val agent: Agent? = null,
    val posts: List<AgentPost> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AgentDetailViewModel @Inject constructor(
    private val useCases: AgentUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(AgentDetailUiState())
    val uiState: StateFlow<AgentDetailUiState> = _uiState

    fun agentDetails(agentId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                coroutineScope {
                    val agentDeferred = async {
                        useCases.getAgentById(agentId).getOrThrow()
                    }

                    val postsDeferred = async {
                        useCases.getAgentPosts(agentId).getOrThrow()
                    }

                    val agent = agentDeferred.await()
                    val posts = postsDeferred.await()

                    _uiState.update {
                        it.copy(
                            agent = agent,
                            posts = posts,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = e.message ?: "Failed to load agent details",
                        isLoading = false
                    )
                }
            }
        }
    }
}