package com.abhishek.agentapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.abhishek.agentapp.data.db.entity.AgentEntity

@Dao
interface AgentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAgents(agents: List<AgentEntity>)

    @Query("SELECT * FROM agents ORDER BY lastUpdated DESC LIMIT :limit OFFSET :offset")
    suspend fun getAgents(limit: Int, offset: Int): List<AgentEntity>

    @Query("SELECT * FROM agents WHERE firstName LIKE :query OR lastName LIKE :query OR email LIKE :query")
    suspend fun searchAgents(query: String): List<AgentEntity>

    @Query("SELECT * FROM agents WHERE id = :id")
    suspend fun getAgentById(id: Int): AgentEntity?

    @Query("DELETE FROM agents")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM agents")
    suspend fun getCount(): Int
}