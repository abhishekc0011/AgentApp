package com.abhishek.agentapp.domain.usecase

import com.abhishek.agentapp.domain.model.Agent
import com.abhishek.agentapp.domain.repository.AgentRepository
import javax.inject.Inject

class GetAgentByIdUseCase @Inject constructor(
    private val repository: AgentRepository
) {
    suspend operator fun invoke(agentId: Int): Result<Agent> =
        repository.getUser(agentId)
}