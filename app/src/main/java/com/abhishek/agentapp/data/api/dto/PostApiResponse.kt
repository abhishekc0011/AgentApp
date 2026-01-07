package com.abhishek.agentapp.data.api.dto

import com.abhishek.agentapp.domain.model.AgentPost
import com.google.gson.annotations.SerializedName

data class PostResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("body") val body: String,
    @SerializedName("userId") val userId: Int,
    @SerializedName("likes") val likes: Int = 0,
    @SerializedName("views") val views: Int = 0
) {
    fun toDomain() = AgentPost(
        id = id,
        userId = userId,
        title = title,
        body = body,
        likes = likes,
        views = views
    )
}

data class PostsListResponse(
    @SerializedName("posts") val posts: List<PostResponse>,
    @SerializedName("total") val total: Int
)