package com.abhishek.agentapp.domain.repository

import com.abhishek.agentapp.domain.model.Agent
import com.abhishek.agentapp.domain.model.AgentPost

interface AgentRepository {
    suspend fun getAgents(offset: Int = 0, limit: Int = 20): Result<List<Agent>>
    suspend fun searchAgents(query: String): Result<List<Agent>>
    suspend fun getAgentById(id: Int): Result<Agent>
    suspend fun getUser(id: Int): Result<Agent>
    suspend fun getAgentPosts(userId: Int): Result<List<AgentPost>>
    suspend fun refreshAgents(): Result<List<Agent>>
}