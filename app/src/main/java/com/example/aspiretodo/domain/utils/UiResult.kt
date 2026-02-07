package com.example.aspiretodo.domain.utils

sealed class UiResult<out T> {
    data class Success<T>(val data: T) : UiResult<T>()
    data class Error(val message: String?) : UiResult<Nothing>()
    object Loading : UiResult<Nothing>()
}