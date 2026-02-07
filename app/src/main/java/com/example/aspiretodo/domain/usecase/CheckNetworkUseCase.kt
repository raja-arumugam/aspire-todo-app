package com.example.aspiretodo.domain.usecase

import com.example.aspiretodo.domain.repository.NetworkRepository
import javax.inject.Inject

class CheckNetworkUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend operator fun invoke(): Boolean {
        return networkRepository.isNetworkAvailable()
    }
}