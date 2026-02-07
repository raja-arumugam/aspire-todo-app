package com.example.aspiretodo.presentation.navigation

import com.example.aspiretodo.R

fun Screen.titleRes(): Int {
    return when (this) {
        Screen.TodoList -> R.string.todo_list_title
        Screen.AddEditTodo -> R.string.add_todo_title
    }
}

enum class NavRoute(val value: String) {
    TODO_LIST(value = "todo_list"),
    ADD_EDIT_TODO(value = "add_edit")
}

enum class NavArg(val value: String) {
    TODO_ID(value = "todoId")
}