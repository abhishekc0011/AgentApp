package com.abhishek.agentapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.abhishek.agentapp.data.db.dao.AgentDao
import com.abhishek.agentapp.data.db.dao.PostDao
import com.abhishek.agentapp.data.db.entity.AgentEntity
import com.abhishek.agentapp.data.db.entity.PostEntity

@Database(entities = [AgentEntity::class, PostEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun agentDao(): AgentDao
    abstract fun postDao(): PostDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "agent_directory.db"
                ).build().also { instance = it }
            }
        }
    }
}