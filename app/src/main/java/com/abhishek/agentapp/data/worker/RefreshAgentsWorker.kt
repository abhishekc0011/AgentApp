package com.abhishek.agentapp.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.abhishek.agentapp.data.db.AppDatabase
import com.abhishek.agentapp.data.repository.AgentRepositoryImpl
import com.abhishek.agentapp.di.NetworkModule
import com.abhishek.agentapp.domain.repository.AgentRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

//class RefreshAgentsWorker @AssistedInject constructor(
//    @Assisted context: Context,
//    @Assisted params: WorkerParameters,
//    private val repository: AgentRepository
//) : CoroutineWorker(context, params) {
//
//    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
//        return@withContext try {
//            val result = repository.refreshAgents()
//            if (result.isSuccess) Result.success() else Result.retry()
//        } catch (e: Exception) {
//            Result.retry()
//        }
//    }
//}
@HiltWorker
class RefreshAgentsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        return@withContext try {
            val apiService = NetworkModule.provideApiService(
                NetworkModule.provideRetrofit(NetworkModule.provideOkHttpClient())
            )
            val database = AppDatabase.getInstance(applicationContext)
            val repository = AgentRepositoryImpl(
                apiService,
                database.agentDao(),
                database.postDao()
            )

            val result = repository.refreshAgents()
            if (result.isSuccess) Result.success() else Result.retry()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}