package com.abhishek.agentapp.data.repository

import com.abhishek.agentapp.data.api.ApiService
import com.abhishek.agentapp.data.db.dao.AgentDao
import com.abhishek.agentapp.data.db.dao.PostDao
import com.abhishek.agentapp.data.db.entity.AgentEntity
import com.abhishek.agentapp.data.db.entity.PostEntity
import com.abhishek.agentapp.domain.model.Agent
import com.abhishek.agentapp.domain.model.AgentPost
import com.abhishek.agentapp.domain.repository.AgentRepository
import javax.inject.Inject
import kotlin.collections.emptyList

class AgentRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val agentDao: AgentDao,
    private val postDao: PostDao
) : AgentRepository {

    override suspend fun getAgents(offset: Int, limit: Int): Result<List<Agent>> = try {
        val response = apiService.getUsers(limit = limit, skip = offset)
        if (response.isSuccessful) {
            val users = response.body()?.users?.map { it.toDomain() } ?: emptyList()
            agentDao.insertAgents(users.map { it.toEntity() })
            Result.success(users)
        } else {
            val cached = agentDao.getAgents(limit, offset)
            Result.success(cached.map { it.toDomain() })
        }
    } catch (e: Exception) {
        try {
            val cached = agentDao.getAgents(limit, offset)
            Result.success(cached.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchAgents(query: String): Result<List<Agent>> = try {
        val response = apiService.searchUsers(query)
        if (response.isSuccessful) {
            val users = response.body()?.users?.map { it.toDomain() } ?: emptyList()
            agentDao.insertAgents(users.map { it.toEntity() })
            Result.success(users)
        } else {
            val cached = agentDao.searchAgents("%$query%")
            Result.success(cached.map { it.toDomain() })
        }
    } catch (e: Exception) {
        try {
            val cached = agentDao.searchAgents("%$query%")
            Result.success(cached.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAgentById(id: Int): Result<Agent> = try {
        val cached = agentDao.getAgentById(id)
        if (cached != null) {
            Result.success(cached.toDomain())
        }
        Result.failure(Exception("Agent not found"))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getUser(id: Int): Result<Agent> = try {
        val response = apiService.getAgentByID(id)
        if (response.isSuccessful) {
            val user = response.body()
            if (user != null) {
                // Convert UserResponse to Agent, then to Entity for caching
                val agent = user.toDomain()
                agentDao.insertAgents(listOf(agent.toEntity()))  // Use UserResponse.toEntity()
                Result.success(agent)
            } else {
                val cached = agentDao.getAgentById(id)
                if (cached != null) {
                    Result.success(cached.toDomain())
                } else {
                    Result.failure(Exception("Agent not found"))
                }
            }
        } else {
            val cached = agentDao.getAgentById(id)
            if (cached != null) {
                Result.success(cached.toDomain())
            } else {
                Result.failure(Exception("Agent not found"))
            }
        }
    } catch (e: Exception) {
        try {
            val cached = agentDao.getAgentById(id)
            if (cached != null) {
                Result.success(cached.toDomain())
            } else {
                Result.failure(Exception("Agent not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun getAgentPosts(userId: Int): Result<List<AgentPost>> = try {
        val response = apiService.getUserPosts(userId)
        if (response.isSuccessful) {
            val posts = response.body()?.posts?.map { it.toDomain() } ?: emptyList()
            postDao.insertPosts(posts.map { it.toEntity() })
            Result.success(posts)
        } else {
            val cached = postDao.getPostsByUser(userId)
            Result.success(cached.map { it.toDomain() })
        }
    } catch (e: Exception) {
        try {
            val cached = postDao.getPostsByUser(userId)
            Result.success(cached.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun refreshAgents(): Result<List<Agent>> =
        getAgents(offset = 0, limit = 100)

    private fun Agent.toEntity() = AgentEntity(
        id = id,
        firstName = firstName,
        lastName = lastName,
        email = email,
        phone = phone,
        image = image,
        company = company,
        lastUpdated = lastUpdated
    )

    private fun AgentPost.toEntity() = PostEntity(
        id = id,
        userId = userId,
        title = title,
        body = body,
        likes = likes,
        views = views,
        timestamp = timestamp
    )
}