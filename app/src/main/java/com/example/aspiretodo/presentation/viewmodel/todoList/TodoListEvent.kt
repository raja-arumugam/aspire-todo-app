package com.example.aspiretodo.presentation.viewmodel.todoList

sealed class TodoListEvent {
    data object LoadTodos : TodoListEvent()
    data class DeleteTodo(val todoId: String) : TodoListEvent()
    data object ClearError : TodoListEvent()
}