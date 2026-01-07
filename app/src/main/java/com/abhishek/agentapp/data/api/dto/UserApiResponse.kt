package com.abhishek.agentapp.data.api.dto

import com.abhishek.agentapp.domain.model.Agent
import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("image") val image: String,
    @SerializedName("company") val company: UserCompany
) {
    fun toDomain() = Agent(
        id = id,
        firstName = firstName,
        lastName = lastName,
        email = email,
        phone = phone,
        image = image,
        company = company.name
    )
}

data class UserCompany(
    @SerializedName("name") val name: String
)

data class UsersListResponse(
    @SerializedName("users") val users: List<UserResponse>,
    @SerializedName("total") val total: Int,
    @SerializedName("skip") val skip: Int,
    @SerializedName("limit") val limit: Int
)