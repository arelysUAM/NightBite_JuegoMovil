package ni.edu.uam.nightbiteapp.viewmodel

import ni.edu.uam.nightbiteapp.data.remote.dto.UserResponse

sealed class ProfileUiState {
    object Idle : ProfileUiState()
    object Loading : ProfileUiState()

    data class Success(
        val user: UserResponse
    ) : ProfileUiState()

    data class Error(
        val message: String
    ) : ProfileUiState()
}