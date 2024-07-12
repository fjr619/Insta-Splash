package com.fjr619.instasplash.domain.repository

import com.fjr619.instasplash.domain.model.NetworkStatus
import kotlinx.coroutines.flow.StateFlow

interface NetworkConnectivityObserver {
    val networkStatus: StateFlow<NetworkStatus>
}