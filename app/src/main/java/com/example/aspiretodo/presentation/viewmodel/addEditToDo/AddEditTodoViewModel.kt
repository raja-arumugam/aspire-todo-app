package com.example.aspiretodo.presentation.viewmodel.addEditToDo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aspiretodo.domain.model.Todo
import com.example.aspiretodo.domain.usecase.TodoUseCases
import com.example.aspiretodo.presentation.utils.BottomSheetState
import com.example.aspiretodo.presentation.utils.BottomSheetType
import com.example.aspiretodo.presentation.utils.ValidationMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.aspiretodo.domain.utils.UiResult
import com.example.aspiretodo.presentation.navigation.NavArg
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.util.Date
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class AddEditTodoViewModel @Inject constructor(
    private val useCases: TodoUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(value = AddEditTodoUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect =
        MutableSharedFlow<AddEditTodoEffect>(
            replay = 0,
            extraBufferCapacity = 1
        )
    val uiEffect = _uiEffect.asSharedFlow()

    private val todoId: String? =
        savedStateHandle.get<String>(NavArg.TODO_ID.value)

    init {
        if (!todoId.isNullOrEmpty()) {
            getByIdToDo(todoId)
        }
    }

    fun onEvent(event: AddEditTodoEvent) {
        when (event) {
            is AddEditTodoEvent.TitleChanged ->
                _uiState.update { it.copy(title = event.value) }

            is AddEditTodoEvent.DescriptionChanged ->
                _uiState.update { it.copy(description = event.value) }

            is AddEditTodoEvent.SaveTodo -> saveTodo()

            is AddEditTodoEvent.StatusChanged ->
                _uiState.update { it.copy(isCompleted = event.isCompleted) }

            is AddEditTodoEvent.ClearError -> {
                _uiState.update {
                    it.copy(bottomSheetState = BottomSheetState())
                }
            }
        }
    }

    private fun getByIdToDo(id: String) {
        viewModelScope.launch {
            todoId?.let { useCases.getByIdToDo(todoId = id) }?.collect { result ->
                when (result) {
                    UiResult.Loading -> _uiState.update { it.copy(isLoading = true) }

                    is UiResult.Success -> _uiState.update {
                        it.copy(
                            isLoading = false,
                            title = result.data.title,
                            description = result.data.description ?: "",
                            isCompleted = result.data.isCompleted
                        )
                    }

                    is UiResult.Error -> {
                        _uiState.update { it.copy(isLoading = false) }
                        showBottomSheet(message = result.message ?: ValidationMessage.ERROR_OCCURRED.message, type = BottomSheetType.ERROR)
                    }
                }
            }
        }
    }

    private fun saveTodo() {
        if (_uiState.value.title.isBlank()) {
            showBottomSheet(
                message = ValidationMessage.TITLE_EMPTY.message,
                type = BottomSheetType.ERROR
            )
            return
        }

        if (_uiState.value.description.isBlank()) {
            showBottomSheet(
                message = ValidationMessage.DESCRIPTION_EMPTY.message,
                type = BottomSheetType.ERROR
            )
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val flow: Flow<UiResult<Unit>> =
                if (todoId == null) {
                    useCases.addTodo(
                        Todo(
                            id = UUID.randomUUID().toString(),
                            title = _uiState.value.title,
                            description = _uiState.value.description,
                            createdAt = Date(),
                            isCompleted = _uiState.value.isCompleted
                        )
                    )
                } else {
                    useCases.updateTodo(
                        Todo(
                            id = todoId,
                            title = _uiState.value.title,
                            description = _uiState.value.description,
                            createdAt = Date(),
                            isCompleted = _uiState.value.isCompleted
                        )
                    )
                }

            val result = flow.first { it !is UiResult.Loading }

            when (result) {
                is UiResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }

                    val successMessage =
                        if (todoId == null) {
                            ValidationMessage.SAVE_SUCCESS.message
                        } else {
                            ValidationMessage.UPDATE_SUCCESS.message
                        }

                    showBottomSheet(
                        message = successMessage,
                        type = BottomSheetType.SUCCESS
                    )

                    delay(timeMillis = 3500)

                    _uiState.update {
                        it.copy(bottomSheetState = BottomSheetState())
                    }

                    _uiEffect.emit(value = AddEditTodoEffect.NavigateBack)
                }

                is UiResult.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    showBottomSheet(
                        message = result.message ?: ValidationMessage.ERROR_OCCURRED.message,
                        type = BottomSheetType.ERROR
                    )
                }

                UiResult.Loading -> Unit
            }
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