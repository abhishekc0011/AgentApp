package com.abhishek.agentapp.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.abhishek.agentapp.domain.model.Agent

@Entity(tableName = "agents")
data class AgentEntity(
    @PrimaryKey val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val image: String,
    val company: String,
    val lastUpdated: Long = System.currentTimeMillis()
) {
    fun toDomain() = Agent(
        id = id,
        firstName = firstName,
        lastName = lastName,
        email = email,
        phone = phone,
        image = image,
        company = company,
        lastUpdated = lastUpdated
    )
}