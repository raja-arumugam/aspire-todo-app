package com.example.aspiretodo.domain.usecase

import com.example.aspiretodo.domain.repository.TodoRepository
import com.example.aspiretodo.domain.utils.UiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class DeleteTodoUseCase(
    private val repository: TodoRepository
) {
    operator fun invoke(todoId: String): Flow<UiResult<Unit>> = flow {
        emit(value = UiResult.Loading)
        try {
            repository.deleteTodo(todoId)
            emit(value = UiResult.Success(Unit))
        } catch (e: Exception) {
            emit(value = UiResult.Error(e.message))
        }
    }.flowOn(Dispatchers.IO)
}