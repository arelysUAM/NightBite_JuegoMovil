package ni.edu.uam.nightbiteapp.viewmodel

import ni.edu.uam.nightbiteapp.data.remote.dto.UserResponse

sealed class StartUiState {
    data class Loading(
        val requestId: Long = 0L
    ) : StartUiState()

    data class NavigateToLogin(
        val requestId: Long
    ) : StartUiState()

    data class NavigateToHome(
        val user: UserResponse,
        val requestId: Long
    ) : StartUiState()

    data class ServerError(
        val message: String,
        val requestId: Long
    ) : StartUiState()
}