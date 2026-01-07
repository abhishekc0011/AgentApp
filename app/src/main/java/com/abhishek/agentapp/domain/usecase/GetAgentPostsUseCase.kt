package com.abhishek.agentapp.domain.usecase

import com.abhishek.agentapp.domain.model.AgentPost
import com.abhishek.agentapp.domain.repository.AgentRepository
import javax.inject.Inject

class GetAgentPostsUseCase @Inject constructor(
    private val repository: AgentRepository
) {
    suspend operator fun invoke(userId: Int): Result<List<AgentPost>> =
        repository.getAgentPosts(userId)
}