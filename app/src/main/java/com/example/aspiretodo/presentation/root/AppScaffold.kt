package com.example.aspiretodo.presentation.root

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.aspiretodo.presentation.AppFloatingActionButton
import com.example.aspiretodo.presentation.navigation.AppTopBar
import com.example.aspiretodo.presentation.navigation.Screen

@Composable
fun AppScaffold(
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val currentScreen = when {
        currentRoute?.startsWith(prefix = Screen.AddEditTodo.route) == true ->
            Screen.AddEditTodo
        else ->
            Screen.TodoList
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(
                screen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                onBackClick = { navController.popBackStack() }
            )
        },
        floatingActionButton = {
            if (currentScreen == Screen.TodoList) {
                AppFloatingActionButton(
                    onClick = {
                        navController.navigate(
                            route = Screen.AddEditTodo.createRoute(todoId = null)
                        )
                    }
                )
            }
        }
    ) { padding ->
        content(padding)
    }
}