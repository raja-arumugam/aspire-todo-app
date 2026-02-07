package com.example.aspiretodo.presentation.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.layout.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.aspiretodo.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    screen: Screen,
    canNavigateBack: Boolean,
    onBackClick: () -> Unit
) {
    TopAppBar(
        modifier = Modifier.statusBarsPadding(),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface
        ),
        title = {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(id = screen.titleRes()),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                )

                if (canNavigateBack) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_arrow_back_ios_new_24),
                            contentDescription = stringResource(id = R.string.back),
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(size = 24.dp)
                        )
                    }
                }
            }
        },
        actions = {
            Spacer(modifier = Modifier.width(width = 48.dp))
        }
    )
}