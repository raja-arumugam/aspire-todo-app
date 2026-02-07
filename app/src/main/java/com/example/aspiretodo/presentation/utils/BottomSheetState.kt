package com.example.aspiretodo.presentation.utils

data class BottomSheetState(
    val isVisible: Boolean = false,
    val message: String = "",
    val type: BottomSheetType = BottomSheetType.NONE
)

enum class BottomSheetType { NONE, ERROR, SUCCESS }