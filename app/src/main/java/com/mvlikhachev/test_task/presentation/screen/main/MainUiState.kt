package com.mvlikhachev.test_task.presentation.screen.main

import com.mvlikhachev.test_task.domain.model.Point

sealed class UiState {
    data object Idle : UiState()
    data object Loading : UiState()
    data object Empty : UiState()
    data class Success(val points: List<Point>) : UiState()
    data class Error(val message: String) : UiState()
}