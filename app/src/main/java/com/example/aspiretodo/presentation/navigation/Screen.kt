package com.example.aspiretodo.presentation.navigation

sealed class Screen(val route: String) {

    data object TodoList : Screen(
        route = NavRoute.TODO_LIST.value
    )

    data object AddEditTodo : Screen(
        route = NavRoute.ADD_EDIT_TODO.value
    ) {

        val routeWithArg: String =
            "${NavRoute.ADD_EDIT_TODO.value}?${NavArg.TODO_ID.value}={${NavArg.TODO_ID.value}}"

        fun createRoute(todoId: String?): String {
            return if (todoId == null) {
                NavRoute.ADD_EDIT_TODO.value
            } else {
                "${NavRoute.ADD_EDIT_TODO.value}?${NavArg.TODO_ID.value}=$todoId"
            }
        }
    }
}
