package com.example.aspiretodo.presentation.ui.todoList

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aspiretodo.R
import com.example.aspiretodo.domain.model.Todo
import com.example.aspiretodo.presentation.BottomSheetView
import com.example.aspiretodo.presentation.formatAsString
import com.example.aspiretodo.presentation.viewmodel.todoList.TodoListUiState
import com.example.aspiretodo.ui.utils.Error
import com.example.aspiretodo.ui.utils.Success

@Composable
fun TodoListScreen(
    state: TodoListUiState,
    onTodoClick: (Todo) -> Unit = {},
    onDismiss: () -> Unit,
    onDeleteClick:(Todo) -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize()) {

        if (state.todos.isEmpty() && !state.isLoading) {
            EmptyTodoState()
        } else {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Adaptive(minSize = 160.dp),
                contentPadding = PaddingValues(all = 16.dp),
                verticalArrangement = Arrangement.spacedBy(space = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(space = 12.dp)
            ) {
                items(items = state.todos, key = { it.id }) { todo ->
                    TodoCard(
                        todo = todo,
                        onClick = { onTodoClick(todo) },
                        onDelete =  {
                            onDeleteClick(todo)
                        }
                    )
                }
            }
        }

        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        if (state.bottomSheetState.isVisible) {
            BottomSheetView(
                state = state.bottomSheetState,
                onDismiss = {
                    onDismiss()
                }
            )
        }
    }
}

@Composable
fun TodoCard(
    todo: Todo,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(size = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(all = 16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = todo.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(weight = 1f)
                )

                IconButton(onClick = onDelete) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = stringResource(id = R.string.back),
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(size = 20.dp)
                    )
                }
            }

            if (!todo.description.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = todo.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                modifier = Modifier.wrapContentSize(),
                text = todo.createdAt.formatAsString(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                modifier = Modifier.wrapContentSize(),
                text = if (todo.isCompleted) {
                    stringResource(id = R.string.completed)
                } else {
                    stringResource(id = R.string.pending)
                },
                style = MaterialTheme.typography.bodyMedium,
                color = if (todo.isCompleted) {
                    Success
                } else {
                    Error
                }
            )
        }
    }
}

@Composable
fun EmptyTodoState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_empty),
            contentDescription = null,
            modifier = Modifier.size(size = 120.dp)
        )

        Spacer(modifier = Modifier.height(height = 16.dp))

        Text(
            text = stringResource(id = R.string.empty_list),
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
    }
}