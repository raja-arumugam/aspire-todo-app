package com.example.aspiretodo.data.model

import com.example.aspiretodo.domain.model.Todo
import com.google.firebase.Timestamp

data class TodoDto(
    var title: String = "",
    var description: String? = null,
    var createdAt: Timestamp = Timestamp.now(),
    var completed: Boolean = false
) {
    fun toDomain(id: String): Todo {
        return Todo(
            id = id,
            title = title,
            description = description,
            createdAt = createdAt.toDate(),
            isCompleted = completed
        )
    }

    companion object {
        fun fromDomain(todo: Todo): TodoDto {
            return TodoDto(
                title = todo.title,
                description = todo.description,
                createdAt = Timestamp(todo.createdAt),
                completed = todo.isCompleted
            )
        }
    }
}