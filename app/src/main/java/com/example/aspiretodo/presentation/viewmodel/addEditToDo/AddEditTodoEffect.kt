package com.example.aspiretodo.presentation.viewmodel.addEditToDo

sealed class AddEditTodoEffect {
    data object NavigateBack : AddEditTodoEffect()
}