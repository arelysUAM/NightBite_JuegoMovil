package ni.edu.uam.nightbiteapp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import ni.edu.uam.nightbiteapp.data.local.mock.NightLevelsData

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        HomeUiState(
            isLoading = false,
            levels = NightLevelsData.levels,
            errorMessage = null,
            userLoadFailed = false
        )
    )

    val uiState: StateFlow<HomeUiState> = _uiState

    fun loadHomeData(userId: Long?) {
        _uiState.update {
            it.copy(
                isLoading = false,
                levels = NightLevelsData.levels,
                errorMessage = null,
                userLoadFailed = false
            )
        }
    }

    fun clearError() {
        _uiState.update {
            it.copy(
                errorMessage = null,
                userLoadFailed = false
            )
        }
    }
}