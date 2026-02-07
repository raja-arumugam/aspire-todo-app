package com.example.aspiretodo.domain.usecase

data class TodoUseCases(
    val getTodos: GetTodosUseCase,
    val addTodo: AddTodoUseCase,
    val updateTodo: UpdateTodoUseCase,
    val deleteTodo: DeleteTodoUseCase,
    val getByIdToDo: GetTodoByIdUseCase,
    val checkNetwork: CheckNetworkUseCase
)