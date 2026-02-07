package com.example.aspiretodo.domain.model

import java.util.Date

data class Todo(
    val id: String,
    val title: String,
    val description: String?,
    val createdAt: Date,
    val isCompleted: Boolean = false
)