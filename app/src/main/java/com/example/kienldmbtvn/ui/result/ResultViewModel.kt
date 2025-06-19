package com.example.kienldmbtvn.ui.result

import androidx.lifecycle.viewModelScope
import com.example.kienldmbtvn.base.BaseViewModel
import com.example.kienldmbtvn.base.BaseUIState
import com.example.kienldmbtvn.data.utils.FileUtils
import kotlinx.coroutines.launch

data class ResultData(
    val downloadState: BaseUIState<Unit> = BaseUIState.Idle
)

class ResultViewModel : BaseViewModel<BaseUIState<ResultData>>(BaseUIState.Success(ResultData())) {

    private fun updateResultData(update: (ResultData) -> ResultData) {
        val currentState = uiState.value
        if (currentState is BaseUIState.Success) {
            updateState { BaseUIState.Success(update(currentState.data)) }
        }
    }

    private fun getCurrentData(): ResultData? {
        return when (val state = uiState.value) {
            is BaseUIState.Success -> state.data
            else -> null
        }
    }

    fun downloadPhoto(imageUrl: String) {
        viewModelScope.launch {
            updateResultData { it.copy(downloadState = BaseUIState.Loading) }
            
            val result = FileUtils.saveFileToStorage(imageUrl)
            
            result.fold(
                onSuccess = {
                    updateResultData { it.copy(downloadState = BaseUIState.Success(Unit)) }
                    // Reset to idle after a short delay so UI can show success
                    updateResultData { it.copy(downloadState = BaseUIState.Idle) }
                },
                onFailure = { error ->
                    val errorMessage = error.message ?: "Failed to download image"
                    updateResultData { it.copy(downloadState = BaseUIState.Error(errorMessage)) }
                }
            )
        }
    }

    fun clearDownloadState() {
        updateResultData { it.copy(downloadState = BaseUIState.Idle) }
    }
}