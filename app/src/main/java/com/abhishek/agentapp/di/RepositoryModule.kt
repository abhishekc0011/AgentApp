package com.abhishek.agentapp.di

import com.abhishek.agentapp.data.repository.AgentRepositoryImpl
import com.abhishek.agentapp.domain.repository.AgentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindAgentRepository(impl: AgentRepositoryImpl): AgentRepository
}