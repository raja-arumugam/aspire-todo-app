package com.example.aspiretodo.presentation.utils

enum class ValidationMessage(val message: String) {
    TITLE_EMPTY(message = "Title is required"),
    DESCRIPTION_EMPTY(message = "Description is required"),
    ERROR_OCCURRED(message = "Something went wrong"),
    SAVE_SUCCESS(message = "Todo saved successfully"),
    UPDATE_SUCCESS(message = "Todo updated successfully"),
    DATA_NOT_FOUND(message = "Todo not found"),
    NO_INTERNET(message = "No internet connection. Please check your network.")
}