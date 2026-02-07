package com.example.aspiretodo.presentation.viewmodel.todoList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aspiretodo.domain.usecase.TodoUseCases
import com.example.aspiretodo.presentation.utils.BottomSheetState
import com.example.aspiretodo.presentation.utils.ValidationMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.aspiretodo.domain.utils.UiResult
import com.example.aspiretodo.presentation.utils.BottomSheetType
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val useCases: TodoUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(value = TodoListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        onEvent(event = TodoListEvent.LoadTodos)
    }

    fun onEvent(event: TodoListEvent) {
        when (event) {
            TodoListEvent.LoadTodos -> observeTodos()

            is TodoListEvent.DeleteTodo -> deleteTodo(todoId = event.todoId)

            is TodoListEvent.ClearError ->  _uiState.update { current ->
                current.copy(bottomSheetState = BottomSheetState())
            }
        }
    }

    private fun observeTodos() {
        viewModelScope.launch {
            if (!useCases.checkNetwork()) {
                showBottomSheet(
                    message = ValidationMessage.NO_INTERNET.message,
                    type = BottomSheetType.ERROR
                )
                return@launch
            }

            useCases.getTodos()
                .collect { result ->
                    when (result) {
                        is UiResult.Loading -> _uiState.update { it.copy(isLoading = true) }
                        is UiResult.Success -> _uiState.update { it.copy(isLoading = false, todos = result.data) }
                        is UiResult.Error ->
                            showBottomSheet(
                                message = result.message ?: ValidationMessage.ERROR_OCCURRED.message,
                                type = BottomSheetType.ERROR
                            )
                    }
                }
        }
    }

    private fun deleteTodo(todoId: String) {
        viewModelScope.launch {
            useCases.deleteTodo(todoId)
                .onEach { result ->
                    when (result) {
                        UiResult.Loading -> _uiState.update { it.copy(isLoading = true) }

                        is UiResult.Success -> _uiState.update { current ->
                            current.copy(
                                todos = current.todos.filter { it.id != todoId },
                                isLoading = false
                            )
                        }

                        is UiResult.Error -> _uiState.update { current ->
                            current.copy(
                                bottomSheetState = BottomSheetState(
                                    isVisible = true,
                                    message = result.message ?: ValidationMessage.ERROR_OCCURRED.message
                                ),
                                isLoading = false
                            )
                        }
                    }
                }.launchIn(scope = this)
        }
    }

    private fun showBottomSheet(message: String, type: BottomSheetType) {
        _uiState.update {
            it.copy(bottomSheetState = BottomSheetState(
                isVisible = true,
                message = message,
                type = type
            ))
        }
    }
}