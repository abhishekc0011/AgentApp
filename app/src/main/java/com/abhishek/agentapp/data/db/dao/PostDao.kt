package com.abhishek.agentapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.abhishek.agentapp.data.db.entity.PostEntity

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<PostEntity>)

    @Query("SELECT * FROM posts WHERE userId = :userId ORDER BY id DESC LIMIT :limit")
    suspend fun getPostsByUser(userId: Int, limit: Int = 10): List<PostEntity>

    @Query("DELETE FROM posts WHERE userId = :userId")
    suspend fun deleteUserPosts(userId: Int)

    @Query("DELETE FROM posts")
    suspend fun clearAll()
}