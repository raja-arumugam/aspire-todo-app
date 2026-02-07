package com.example.aspiretodo.presentation.viewmodel.todoList

import com.example.aspiretodo.domain.model.Todo
import com.example.aspiretodo.presentation.utils.BottomSheetState

data class TodoListUiState(
    val isLoading: Boolean = false,
    val todos: List<Todo> = emptyList(),
    val error: String? = null,
    val bottomSheetState: BottomSheetState = BottomSheetState()
)