package com.abhishek.agentapp.domain.usecase

import javax.inject.Inject

data class AgentUseCases @Inject constructor(
    val getAgents: GetAgentsUseCase,
    val searchAgents: SearchAgentsUseCase,
    val getAgentById: GetAgentByIdUseCase,
    val getAgentPosts: GetAgentPostsUseCase,
    val refreshAgents: RefreshAgentsUseCase
)