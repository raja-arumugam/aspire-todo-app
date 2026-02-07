package com.example.aspiretodo.domain.usecase

import com.example.aspiretodo.domain.model.Todo
import com.example.aspiretodo.domain.repository.TodoRepository
import com.example.aspiretodo.domain.utils.UiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class UpdateTodoUseCase(
    private val repository: TodoRepository
) {
    operator fun invoke(todo: Todo): Flow<UiResult<Unit>> = flow {
        emit(value = UiResult.Loading)
        repository.updateTodo(todo)
        emit(value = UiResult.Success(Unit))
    }.catch { e ->
        emit(value = UiResult.Error(e.message))
    }
}