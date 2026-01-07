package com.abhishek.agentapp.domain.model

data class Agent(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val image: String,
    val company: String,
    val lastUpdated: Long = System.currentTimeMillis()
) {
    val fullName: String get() = "$firstName $lastName"
}

data class AgentPost(
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String,
    val likes: Int,
    val views: Int,
    val timestamp: Long = System.currentTimeMillis()
)