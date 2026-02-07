package com.example.aspiretodo.domain.usecase

import com.example.aspiretodo.domain.model.Todo
import com.example.aspiretodo.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import com.example.aspiretodo.domain.utils.UiResult
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class GetTodoByIdUseCase(
    private val repository: TodoRepository
) {
    operator fun invoke(todoId: String): Flow<UiResult<Todo>> = flow {
        emit(value = UiResult.Loading)

        repository.getTodoById(todoId)
            .collect { result ->
                emit(value = result)
            }

    }.catch { e ->
        emit(value = UiResult.Error(e.message))
    }
}