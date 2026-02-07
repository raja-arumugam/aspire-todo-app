package com.example.aspiretodo.domain.repository

import com.example.aspiretodo.domain.model.Todo
import kotlinx.coroutines.flow.Flow
import com.example.aspiretodo.domain.utils.UiResult

interface TodoRepository {

    fun getTodos(): Flow<List<Todo>>

    fun getTodoById(todoId: String): Flow<UiResult<Todo>>

    suspend fun addTodo(todo: Todo)

    suspend fun updateTodo(todo: Todo)

    suspend fun deleteTodo(todoId: String)
}