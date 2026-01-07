package com.abhishek.agentapp.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.abhishek.agentapp.data.db.AppDatabase
import com.abhishek.agentapp.util.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val database: AppDatabase,
    private val workManager: WorkManager
) : ViewModel() {

    private val _offlineMode = MutableStateFlow(false)
    val offlineMode: StateFlow<Boolean> = _offlineMode

    private val _autoRefresh = MutableStateFlow(true)
    val autoRefresh: StateFlow<Boolean> = _autoRefresh

    private val _networkStatus = MutableStateFlow(true)
    val networkStatus: StateFlow<Boolean> = _networkStatus

    private val _lastRefreshTime = MutableStateFlow(System.currentTimeMillis())
    val lastRefreshTime: StateFlow<Long> = _lastRefreshTime

    init {
        observeNetworkStatus()
    }

    private fun observeNetworkStatus() {
        viewModelScope.launch {
            NetworkMonitor.isOnline.collect { isOnline ->
                _networkStatus.value = isOnline
            }
        }
    }

    fun setOfflineMode(enabled: Boolean) {
        _offlineMode.value = enabled
        if (enabled) {
            workManager.cancelUniqueWork("agent_refresh")
        }
    }

    fun setAutoRefresh(enabled: Boolean) {
        _autoRefresh.value = enabled
        if (!enabled) {
            workManager.cancelUniqueWork("agent_refresh")
        }
    }

    fun clearCache() {
        viewModelScope.launch {
            database.agentDao().clearAll()
            database.postDao().clearAll()
            _lastRefreshTime.value = System.currentTimeMillis()
        }
    }
}