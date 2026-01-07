package com.abhishek.agentapp.di

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.abhishek.agentapp.data.worker.RefreshAgentsWorker
import com.abhishek.agentapp.domain.repository.AgentRepository
import javax.inject.Inject

class HiltWorkerFactory @Inject constructor(
    private val repository: AgentRepository
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? = when (workerClassName) {
        RefreshAgentsWorker::class.java.name -> {
            RefreshAgentsWorker(appContext, workerParameters)
        }
        else -> null
    }
}
