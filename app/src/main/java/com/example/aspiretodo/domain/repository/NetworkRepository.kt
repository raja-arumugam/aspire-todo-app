package com.example.aspiretodo.domain.repository

import com.example.aspiretodo.domain.utils.NetworkStatus
import kotlinx.coroutines.flow.Flow

interface NetworkRepository {
    fun observeNetwork(): Flow<NetworkStatus>
    suspend fun isNetworkAvailable(): Boolean
}