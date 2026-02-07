package com.example.aspiretodo.presentation.viewmodel.addEditToDo

sealed class AddEditTodoEvent {
    data class TitleChanged(val value: String) : AddEditTodoEvent()
    data class DescriptionChanged(val value: String) : AddEditTodoEvent()
    data class StatusChanged(val isCompleted: Boolean) : AddEditTodoEvent()
    object SaveTodo : AddEditTodoEvent()
    data object ClearError : AddEditTodoEvent()
}