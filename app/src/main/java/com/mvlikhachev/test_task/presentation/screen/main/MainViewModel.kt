package com.mvlikhachev.test_task.presentation.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvlikhachev.test_task.domain.model.Point
import com.mvlikhachev.test_task.domain.usecase.PointsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: PointsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun fetchPoints(count: Int) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val points = repository.invoke(count)

                if (points.isNotEmpty()) {
                    _uiState.value = UiState.Success(points)
                } else {
                    _uiState.value = UiState.Empty
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.localizedMessage ?: "Unknown Error")
            }
        }
    }

    fun clearState() {
        _uiState.update {
            UiState.Idle
        }
    }

}