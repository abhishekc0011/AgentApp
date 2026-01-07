package com.abhishek.agentapp.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.abhishek.agentapp.data.worker.RefreshAgentsWorker
import com.abhishek.agentapp.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by viewModels()

    @Inject
    lateinit var workManager: WorkManager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        // Offline mode toggle
        binding.switchOfflineMode.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setOfflineMode(isChecked)
            if (isChecked) {
                // Cancel background refresh when offline
                workManager.cancelUniqueWork("agent_refresh")
            } else {
                // Resume background refresh
                scheduleRefresh()
            }
        }

        // Auto refresh toggle
        binding.switchAutoRefresh.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setAutoRefresh(isChecked)
            if (isChecked) {
                scheduleRefresh()
            } else {
                workManager.cancelUniqueWork("agent_refresh")
            }
        }

        binding.buttonClearCache.setOnClickListener {
            viewModel.clearCache()
        }
    }

    private fun scheduleRefresh() {
        val refreshWork = PeriodicWorkRequestBuilder<RefreshAgentsWorker>(
            15, TimeUnit.MINUTES
        ).setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        ).build()

        workManager.enqueueUniquePeriodicWork(
            "agent_refresh",
            ExistingPeriodicWorkPolicy.KEEP,
            refreshWork
        )
    }


    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.offlineMode.collect { isOffline ->
                        binding.switchOfflineMode.isChecked = isOffline
                    }
                }
                launch {
                    viewModel.autoRefresh.collect { isEnabled ->
                        binding.switchAutoRefresh.isChecked = isEnabled
                    }
                }
                launch {
                    viewModel.networkStatus.collect { isOnline ->
                        binding.tvNetworkStatus.text =
                            if (isOnline) "Online" else "Offline"
                        binding.tvNetworkStatus.setTextColor(
                            if (isOnline) 0xFF4CAF50.toInt() else 0xFFFF9800.toInt()
                        )
                    }
                }
                launch {
                    viewModel.lastRefreshTime.collect { timestamp ->
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                        val formattedDate = dateFormat.format(Date(timestamp))
                        binding.tvLastRefresh.text = "Last refresh: $formattedDate"
                    }
                }
            }
        }
    }
}