package ni.edu.uam.nightbiteapp.viewmodel

import ni.edu.uam.nightbiteapp.data.remote.dto.UserResponse

/**
 * Representa los estados posibles del inicio de sesión.
 */
sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()

    data class Success(
        val user: UserResponse
    ) : LoginUiState()

    data class Error(
        val message: String,
        val type: LoginErrorType
    ) : LoginUiState()
}

enum class LoginErrorType {
    UserNotFound,
    InvalidCredentials,
    IncompleteFields,
    ConnectionError
}