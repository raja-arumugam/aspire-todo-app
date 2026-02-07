package com.example.aspiretodo.data.repository

import com.example.aspiretodo.data.model.TodoDto
import com.example.aspiretodo.domain.model.Todo
import com.example.aspiretodo.domain.repository.TodoRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import com.example.aspiretodo.domain.utils.UiResult
import com.example.aspiretodo.presentation.utils.ValidationMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    firestore: FirebaseFirestore
) : TodoRepository {

    private val todosCollection = firestore.collection("todos")

    override fun getTodos(): Flow<List<Todo>> = callbackFlow {
        val listener = todosCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(cause = error)
                return@addSnapshotListener
            }

            val todos = snapshot?.documents
                ?.mapNotNull { doc ->
                    doc.toObject(TodoDto::class.java)
                        ?.toDomain(id = doc.id)
                }
                .orEmpty()

            trySend(todos)
        }

        awaitClose { listener.remove() }
    }

    override fun getTodoById(todoId: String): Flow<UiResult<Todo>> = flow {
        emit(value = UiResult.Loading)

        try {
            val snapshot = todosCollection.document(todoId).get().await()

            val todo = snapshot.toObject(TodoDto::class.java)
                ?.toDomain(id = snapshot.id)

            if (todo != null) {
                emit(value = UiResult.Success(todo))
            } else {
                emit(value = UiResult.Error(message = ValidationMessage.DATA_NOT_FOUND.message))
            }
        } catch (e: Exception) {
            emit(value = UiResult.Error(message = e.message))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun addTodo(todo: Todo) {
        val dto = TodoDto.fromDomain(todo)

        todosCollection
            .document(todo.id)
            .set(dto)
            .await()
    }

    override suspend fun updateTodo(todo: Todo) {
        todosCollection.document(todo.id)
            .update(
                mapOf(
                    "title" to todo.title,
                    "description" to todo.description,
                    "completed" to todo.isCompleted
                )
            )
            .await()
    }

    override suspend fun deleteTodo(todoId: String) {
        todosCollection
            .document(todoId)
            .delete()
            .await()
    }
}