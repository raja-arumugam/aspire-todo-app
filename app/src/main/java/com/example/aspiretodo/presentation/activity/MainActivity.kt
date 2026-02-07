package com.example.aspiretodo.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.aspiretodo.presentation.navigation.TodoNavGraph
import com.example.aspiretodo.presentation.root.AppScaffold
import com.example.aspiretodo.ui.utils.AspireTodoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AspireTodoTheme {
                val navController = rememberNavController()

                AppScaffold(navController = navController) { padding ->
                    TodoNavGraph(
                        navController = navController,
                        paddingValues = padding
                    )
                }
            }
        }
    }
}