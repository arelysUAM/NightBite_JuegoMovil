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
import ni.edu.uam.nightbiteapp.ui.validation.AccountValidators

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

        if (wantsToChangeUsername) {
            val usernameError = AccountValidators.validateUsername(state.newUsername)

            if (usernameError != null) {
                _uiState.value = state.copy(
                    errorMessage = usernameError
                )
                return
            }
        }

        if (state.newUsername.isNotBlank() && state.newUsername == currentUsername) {
            _uiState.value = state.copy(
                errorMessage = "El nuevo nombre de usuario debe ser diferente al actual."
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

            val currentPasswordError = AccountValidators.validatePassword(
                password = state.currentPassword,
                fieldName = "La contraseña actual"
            )

            if (currentPasswordError != null) {
                _uiState.value = state.copy(
                    errorMessage = currentPasswordError
                )
                return
            }

            val newPasswordError = AccountValidators.validatePassword(
                password = state.newPassword,
                fieldName = "La nueva contraseña"
            )

            if (newPasswordError != null) {
                _uiState.value = state.copy(
                    errorMessage = newPasswordError
                )
                return
            }

            val confirmPasswordError = AccountValidators.validateNewPasswordConfirmation(
                newPassword = state.newPassword,
                confirmNewPassword = state.confirmNewPassword
            )

            if (confirmPasswordError != null) {
                _uiState.value = state.copy(
                    errorMessage = confirmPasswordError
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
                            newUsername = state.newUsername.trim().lowercase()
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

    fun onDeleteAccountClick() {
        _uiState.value = _uiState.value.copy(
            showDeleteAccountDialog = true,
            errorMessage = null
        )
    }

    fun dismissDeleteAccountDialog() {
        _uiState.value = _uiState.value.copy(
            showDeleteAccountDialog = false
        )
    }

    fun deleteAccount(userId: Long?) {
        if (userId == null) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "No se pudo identificar la cuenta del usuario.",
                showDeleteAccountDialog = false
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                showDeleteAccountDialog = false
            )

            try {
                val response = userRepository.deleteUser(userId)

                if (response.isSuccessful) {
                    sessionManager.clearSession()

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        showAccountDeletedDialog = true
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "No se pudo eliminar la cuenta."
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error de conexión con la API."
                )
            }
        }
    }

    fun finishAccountDeletedFlow(onNavigateToLogin: () -> Unit) {
        _uiState.value = AccountCredentialsUiState()
        onNavigateToLogin()
    }
}