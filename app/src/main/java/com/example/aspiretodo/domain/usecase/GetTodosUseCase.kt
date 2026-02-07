package com.example.aspiretodo.domain.usecase

import com.example.aspiretodo.domain.model.Todo
import com.example.aspiretodo.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.example.aspiretodo.domain.utils.UiResult
import kotlinx.coroutines.flow.catch

class GetTodosUseCase(
    private val repository: TodoRepository
) {
    operator fun invoke(): Flow<UiResult<List<Todo>>> = flow {
        emit(value = UiResult.Loading)
        repository.getTodos()
            .collect { todos ->
                emit(value = UiResult.Success(data = todos))
            }
    }.catch { e ->
        emit(value = UiResult.Error(message = e.message))
    }
}