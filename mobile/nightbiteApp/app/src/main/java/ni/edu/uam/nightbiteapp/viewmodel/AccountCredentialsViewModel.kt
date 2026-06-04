package ni.edu.uam.nightbiteapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ni.edu.uam.nightbiteapp.data.local.session.SessionManager
import ni.edu.uam.nightbiteapp.data.remote.dto.UpdatePasswordRequest
import ni.edu.uam.nightbiteapp.data.remote.dto.UpdateUsernameRequest
import ni.edu.uam.nightbiteapp.data.repository.UserRepository

class AccountCredentialsViewModel(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountCredentialsUiState())
    val uiState: StateFlow<AccountCredentialsUiState> = _uiState.asStateFlow()

    fun onNewUsernameChange(value: String) {
        _uiState.value = _uiState.value.copy(
            newUsername = value,
            errorMessage = null
        )
    }

    fun onCurrentPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(
            currentPassword = value,
            errorMessage = null
        )
    }

    fun onNewPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(
            newPassword = value,
            errorMessage = null
        )
    }

    fun onConfirmNewPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(
            confirmNewPassword = value,
            errorMessage = null
        )
    }

    fun onApplyChangesClick(currentUsername: String) {
        val state = _uiState.value

        val wantsToChangeUsername =
            state.newUsername.isNotBlank() && state.newUsername != currentUsername

        val wantsToChangePassword =
            state.currentPassword.isNotBlank() ||
                    state.newPassword.isNotBlank() ||
                    state.confirmNewPassword.isNotBlank()

        if (!wantsToChangeUsername && !wantsToChangePassword) {
            _uiState.value = state.copy(
                errorMessage = "Debes ingresar un nuevo usuario o una nueva contraseña."
            )
            return
        }

        if (state.newUsername.isNotBlank() && state.newUsername == currentUsername) {
            _uiState.value = state.copy(
                errorMessage = "El nuevo nombre de usuario debe ser diferente al actual."
            )
            return
        }

        if (wantsToChangeUsername && !isValidUsername(state.newUsername)) {
            _uiState.value = state.copy(
                errorMessage = "El usuario solo puede usar minúsculas, números y guion bajo. Máximo 16 caracteres."
            )
            return
        }

        if (wantsToChangePassword) {
            if (
                state.currentPassword.isBlank() ||
                state.newPassword.isBlank() ||
                state.confirmNewPassword.isBlank()
            ) {
                _uiState.value = state.copy(
                    errorMessage = "Para cambiar la contraseña debes completar todos los campos de contraseña."
                )
                return
            }

            if (state.newPassword.length < 6) {
                _uiState.value = state.copy(
                    errorMessage = "La nueva contraseña debe tener al menos 6 caracteres."
                )
                return
            }

            if (state.newPassword != state.confirmNewPassword) {
                _uiState.value = state.copy(
                    errorMessage = "La nueva contraseña y la confirmación no coinciden."
                )
                return
            }

            if (state.currentPassword == state.newPassword) {
                _uiState.value = state.copy(
                    errorMessage = "La nueva contraseña debe ser diferente a la actual."
                )
                return
            }
        }

        _uiState.value = state.copy(
            showConfirmDialog = true,
            errorMessage = null
        )
    }

    fun dismissConfirmDialog() {
        _uiState.value = _uiState.value.copy(showConfirmDialog = false)
    }

    fun applyConfirmedChanges(
        userId: Long?,
        currentUsername: String
    ) {
        if (userId == null) {
            _uiState.value = _uiState.value.copy(
                showConfirmDialog = false,
                errorMessage = "No se pudo identificar al usuario activo."
            )
            return
        }

        viewModelScope.launch {
            val state = _uiState.value

            _uiState.value = state.copy(
                isLoading = true,
                showConfirmDialog = false,
                errorMessage = null
            )

            try {
                val wantsToChangeUsername =
                    state.newUsername.isNotBlank() && state.newUsername != currentUsername

                val wantsToChangePassword =
                    state.currentPassword.isNotBlank() &&
                            state.newPassword.isNotBlank() &&
                            state.confirmNewPassword.isNotBlank()

                if (wantsToChangeUsername) {
                    val usernameResponse = userRepository.updateUsername(
                        userId = userId,
                        request = UpdateUsernameRequest(
                            newUsername = state.newUsername.trim()
                        )
                    )

                    if (!usernameResponse.isSuccessful) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "No se pudo actualizar el nombre de usuario."
                        )
                        return@launch
                    }
                }

                if (wantsToChangePassword) {
                    val passwordResponse = userRepository.updatePassword(
                        userId = userId,
                        request = UpdatePasswordRequest(
                            currentPassword = state.currentPassword,
                            newPassword = state.newPassword,
                            confirmNewPassword = state.confirmNewPassword
                        )
                    )

                    if (!passwordResponse.isSuccessful) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "No se pudo actualizar la contraseña. Verifica la contraseña actual."
                        )
                        return@launch
                    }
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    showSessionExpiredDialog = true,
                    successMessage = "Credenciales actualizadas correctamente."
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error de conexión con el servidor."
                )
            }
        }
    }

    fun clearSessionAndFinish(onSessionCleared: () -> Unit) {
        viewModelScope.launch {
            sessionManager.clearSession()
            onSessionCleared()
        }
    }

    private fun isValidUsername(username: String): Boolean {
        val regex = Regex("^[a-z0-9_]{1,16}$")
        return regex.matches(username)
    }
}