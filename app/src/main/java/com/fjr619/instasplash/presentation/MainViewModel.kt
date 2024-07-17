package com.fjr619.instasplash.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fjr619.instasplash.domain.model.NetworkStatus
import com.fjr619.instasplash.domain.repository.NetworkConnectivityObserver
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class MainViewModel(
    private val networkConnectivityObserver: NetworkConnectivityObserver
): ViewModel() {

    val networkStatus: StateFlow<NetworkStatus> = networkConnectivityObserver.networkStatus.stateIn(
        initialValue = NetworkStatus.Disconnected,
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )
}