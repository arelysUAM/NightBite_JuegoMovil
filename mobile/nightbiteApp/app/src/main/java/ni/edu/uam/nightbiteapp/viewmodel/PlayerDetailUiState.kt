package ni.edu.uam.nightbiteapp.viewmodel

import ni.edu.uam.nightbiteapp.data.remote.dto.UserResponse

sealed class PlayerDetailUiState {
    object Idle : PlayerDetailUiState()
    object Loading : PlayerDetailUiState()

    data class Success(
        val user: UserResponse
    ) : PlayerDetailUiState()

    data class Error(
        val message: String
    ) : PlayerDetailUiState()
}