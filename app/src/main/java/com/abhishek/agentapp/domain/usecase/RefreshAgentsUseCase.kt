package com.abhishek.agentapp.domain.usecase

import com.abhishek.agentapp.domain.model.Agent
import com.abhishek.agentapp.domain.repository.AgentRepository
import javax.inject.Inject

class RefreshAgentsUseCase @Inject constructor(
    private val repository: AgentRepository
) {
    suspend operator fun invoke(): Result<List<Agent>> =
        repository.refreshAgents()
}