package com.abhishek.agentapp.di

import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlin.reflect.KClass

@MapKey
@Retention(AnnotationRetention.RUNTIME)
annotation class WorkerKey(val value: KClass<out ListenableWorker>)

@Module
@InstallIn(SingletonComponent::class)
interface WorkerModule {

    @Binds
    fun bindWorkerFactory(factory: HiltWorkerFactory): WorkerFactory
}