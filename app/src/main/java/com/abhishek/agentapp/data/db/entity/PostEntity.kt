package com.abhishek.agentapp.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.abhishek.agentapp.domain.model.AgentPost

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey val id: Int,
    val userId: Int,
    val title: String,
    val body: String,
    val likes: Int,
    val views: Int,
    val timestamp: Long = System.currentTimeMillis()
) {
    fun toDomain() = AgentPost(
        id = id,
        userId = userId,
        title = title,
        body = body,
        likes = likes,
        views = views,
        timestamp = timestamp
    )
}