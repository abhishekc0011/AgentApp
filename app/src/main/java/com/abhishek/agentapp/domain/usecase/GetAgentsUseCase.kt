package com.abhishek.agentapp.domain.usecase

import com.abhishek.agentapp.domain.model.Agent
import com.abhishek.agentapp.domain.repository.AgentRepository
import javax.inject.Inject

class GetAgentsUseCase @Inject constructor(
    private val repository: AgentRepository
) {
    suspend operator fun invoke(offset: Int = 0, limit: Int = 20): Result<List<Agent>> =
        repository.getAgents(offset, limit)
}