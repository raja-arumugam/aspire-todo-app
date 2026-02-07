package com.example.aspiretodo.presentation.navigation

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.aspiretodo.presentation.ui.addEditTodo.AddEditTodoScreen
import com.example.aspiretodo.presentation.ui.todoList.TodoListScreen
import com.example.aspiretodo.presentation.viewmodel.addEditToDo.AddEditTodoViewModel
import com.example.aspiretodo.presentation.viewmodel.todoList.TodoListEvent
import com.example.aspiretodo.presentation.viewmodel.todoList.TodoListViewModel


@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
fun TodoNavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    val todoListViewModel: TodoListViewModel = hiltViewModel()
    val todoListState by todoListViewModel.uiState.collectAsState()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    AnimatedContent(
        targetState = currentRoute,
        transitionSpec = {
            if (
                initialState == Screen.TodoList.route &&
                targetState?.startsWith(Screen.AddEditTodo.route) == true
            ) {
                // Forward navigation
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(durationMillis = 300)
                ) + fadeIn() togetherWith
                        slideOutHorizontally(
                            targetOffsetX = { -it },
                            animationSpec = tween(durationMillis = 300)
                        ) + fadeOut()
            } else {
                // Back navigation
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(durationMillis = 300)
                ) + fadeIn() togetherWith
                        slideOutHorizontally(
                            targetOffsetX = { it },
                            animationSpec = tween(durationMillis = 300)
                        ) + fadeOut()
            }
        },
        label = "nav-animation"
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.TodoList.route,
            modifier = Modifier.padding(paddingValues)
        ) {

            composable(route = Screen.TodoList.route) {
                TodoListScreen(
                    state = todoListState,
                    onTodoClick = { todo ->
                        navController.navigate(
                            route = Screen.AddEditTodo.createRoute(todoId = todo.id)
                        )
                    },
                    onDeleteClick = { todo ->
                        todoListViewModel.onEvent(
                            event = TodoListEvent.DeleteTodo(todoId = todo.id)
                        )
                    },
                    onDismiss = {
                        todoListViewModel.onEvent(
                            event = TodoListEvent.ClearError
                        )
                    }
                )
            }

            composable(
                route = Screen.AddEditTodo.routeWithArg,
                arguments = listOf(
                    navArgument(NavArg.TODO_ID.value) {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    }
                )
            ) {
                val addEditViewModel: AddEditTodoViewModel = hiltViewModel()
                val addEditState by addEditViewModel.uiState.collectAsState()

                AddEditTodoScreen(
                    state = addEditState,
                    snackbarFlow = addEditViewModel.uiEffect,
                    todoId = it.arguments?.getString(NavArg.TODO_ID.value),
                    onEvent = addEditViewModel::onEvent,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}