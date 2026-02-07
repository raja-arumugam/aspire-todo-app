package com.example.aspiretodo.presentation.ui.addEditTodo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.aspiretodo.R
import com.example.aspiretodo.presentation.AppOutlinedDescField
import com.example.aspiretodo.presentation.AppOutlinedTitleField
import com.example.aspiretodo.presentation.AppPrimaryButton
import com.example.aspiretodo.presentation.BottomSheetView
import com.example.aspiretodo.presentation.TodoStatusSwitch
import com.example.aspiretodo.presentation.viewmodel.addEditToDo.AddEditTodoEffect
import com.example.aspiretodo.presentation.viewmodel.addEditToDo.AddEditTodoEvent
import com.example.aspiretodo.presentation.viewmodel.addEditToDo.AddEditTodoUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun AddEditTodoScreen(
    state: AddEditTodoUiState,
    snackbarFlow: SharedFlow<AddEditTodoEffect>,
    todoId: String?,
    onEvent: (AddEditTodoEvent) -> Unit,
    onBack: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = snackbarFlow) {
        snackbarFlow.collect { effect ->
            when (effect) {
                AddEditTodoEffect.NavigateBack -> {
                    focusManager.clearFocus(force = true)
                    keyboardController?.hide()

                    delay(timeMillis = 100)

                    onBack()
                }
            }
        }
    }

    val showLoading =
        todoId != null &&
                state.isLoading &&
                state.title.isBlank() &&
                state.description.isBlank()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .weight(weight = 1f)
                    .verticalScroll(state = rememberScrollState())
            ) {
                // Title
                AppOutlinedTitleField(
                    value = state.title,
                    onValueChange = { onEvent(AddEditTodoEvent.TitleChanged(value = it)) },
                    label = stringResource(id = R.string.title),
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(height = 16.dp))

                // Description
                AppOutlinedDescField(
                    value = state.description,
                    onValueChange = { onEvent(AddEditTodoEvent.DescriptionChanged(value = it)) },
                    label = stringResource(id = R.string.description),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 80.dp, max = 160.dp)
                )

                Spacer(Modifier.height(height = 14.dp))

                TodoStatusSwitch(
                    isCompleted = state.isCompleted,
                    onCheckedChange = {
                        onEvent(AddEditTodoEvent.StatusChanged(isCompleted = it))
                    }
                )
            }

            AppPrimaryButton(
                text = if (todoId == null) stringResource(id = R.string.save) else stringResource(id = R.string.update),
                onClick = { onEvent(AddEditTodoEvent.SaveTodo) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )
        }

        if (showLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        BottomSheetView(
            state = state.bottomSheetState,
            onDismiss = {
                onEvent(AddEditTodoEvent.ClearError)
            }
        )
    }
}