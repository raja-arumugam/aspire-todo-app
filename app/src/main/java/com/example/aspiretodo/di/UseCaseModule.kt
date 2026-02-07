package com.example.aspiretodo.di

import com.example.aspiretodo.domain.repository.NetworkRepository
import com.example.aspiretodo.domain.repository.TodoRepository
import com.example.aspiretodo.domain.usecase.AddTodoUseCase
import com.example.aspiretodo.domain.usecase.CheckNetworkUseCase
import com.example.aspiretodo.domain.usecase.DeleteTodoUseCase
import com.example.aspiretodo.domain.usecase.GetTodoByIdUseCase
import com.example.aspiretodo.domain.usecase.GetTodosUseCase
import com.example.aspiretodo.domain.usecase.TodoUseCases
import com.example.aspiretodo.domain.usecase.UpdateTodoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideTodoUseCases(
        repository: TodoRepository,
        networkRepository: NetworkRepository
    ): TodoUseCases {
        return TodoUseCases(
            getTodos = GetTodosUseCase(repository),
            addTodo = AddTodoUseCase(repository),
            updateTodo = UpdateTodoUseCase(repository),
            deleteTodo = DeleteTodoUseCase(repository),
            getByIdToDo = GetTodoByIdUseCase(repository),
            checkNetwork = CheckNetworkUseCase(networkRepository)
        )
    }
}