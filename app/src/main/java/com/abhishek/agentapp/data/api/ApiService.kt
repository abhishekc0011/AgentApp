package com.abhishek.agentapp.data.api

import com.abhishek.agentapp.data.api.dto.PostsListResponse
import com.abhishek.agentapp.data.api.dto.UserResponse
import com.abhishek.agentapp.data.api.dto.UsersListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    suspend fun getUsers(
        @Query("limit") limit: Int = 20,
        @Query("skip") skip: Int = 0
    ): Response<UsersListResponse>

    @GET("users/search")
    suspend fun searchUsers(
        @Query("q") query: String
    ): Response<UsersListResponse>

    @GET("posts/user/{id}")
    suspend fun getUserPosts(
        @Path("id") userId: Int
    ): Response<PostsListResponse>

    @GET("user/{id}")
    suspend fun getAgentByID(
        @Path("id") userId: Int
    ): Response<UserResponse>
}