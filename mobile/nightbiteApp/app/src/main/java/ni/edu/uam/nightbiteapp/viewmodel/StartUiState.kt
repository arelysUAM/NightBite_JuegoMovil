package ni.edu.uam.nightbiteapp.viewmodel

import ni.edu.uam.nightbiteapp.data.remote.dto.UserResponse

sealed class StartUiState {
    object Loading : StartUiState()

    object NavigateToLogin : StartUiState()

    data class NavigateToHome(
        val user: UserResponse
    ) : StartUiState()

    data class ServerError(
        val message: String
    ) : StartUiState()
}