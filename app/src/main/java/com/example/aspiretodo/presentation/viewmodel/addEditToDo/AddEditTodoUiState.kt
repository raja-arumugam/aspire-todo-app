package com.example.aspiretodo.presentation.viewmodel.addEditToDo

import com.example.aspiretodo.presentation.utils.BottomSheetState
import java.util.Date

data class AddEditTodoUiState(
    val title: String = "",
    val description: String = "",
    val isCompleted: Boolean = false,
    val isLoading: Boolean = false,
    val createdAt: Date? = null,
    val bottomSheetState: BottomSheetState = BottomSheetState()
)