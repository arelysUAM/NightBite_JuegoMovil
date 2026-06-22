package ni.edu.uam.nightbiteapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ni.edu.uam.nightbiteapp.data.remote.dto.UpdatePasswordRequest
import ni.edu.uam.nightbiteapp.data.repository.UserRepository
import ni.edu.uam.nightbiteapp.ui.validation.AccountValidators

class PasswordViewModel(
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(PasswordUiState())
    val uiState: StateFlow<PasswordUiState> = _uiState.asStateFlow()

    private var currentPasswordCheckJob: Job? = null
    private var rejectedCurrentPassword: String = ""

    fun onCurrentPasswordChange(
        value: String,
        userId: Long?
    ) {
        currentPasswordCheckJob?.cancel()

        val localError = validateCurrentPassword(value)

        _uiState.update { state ->
            val newPasswordError = if (state.newPassword.isBlank()) {
                null
            } else {
                validateNewPassword(
                    currentPassword = value,
                    newPassword = state.newPassword
                )
            }

            val confirmPasswordError = if (state.confirmNewPassword.isBlank()) {
                null
            } else {
                validateConfirmPassword(
                    newPassword = state.newPassword,
                    confirmNewPassword = state.confirmNewPassword
                )
            }

            state.copy(
                currentPassword = value,
                currentPasswordError = when {
                    localError != null -> localError
                    value == rejectedCurrentPassword -> "La contraseña actual es incorrecta."
                    else -> null
                },
                currentPasswordStatus = CurrentPasswordStatus.IDLE,
                newPasswordError = newPasswordError,
                confirmPasswordError = confirmPasswordError,
                showInvalidDataDialog = false
            )
        }

        if (
            value.isBlank() ||
            localError != null ||
            userId == null
        ) {
            return
        }

        currentPasswordCheckJob = viewModelScope.launch {
            delay(650L)

            if (_uiState.value.currentPassword != value) {
                return@launch
            }

            _uiState.update {
                it.copy(
                    currentPasswordStatus = CurrentPasswordStatus.CHECKING,
                    currentPasswordError = null
                )
            }

            verifyCurrentPasswordWithBackend(
                userId = userId,
                expectedPassword = value
            )
        }
    }

    fun onNewPasswordChange(value: String) {
        _uiState.update { state ->
            val newPasswordError = validateNewPassword(
                currentPassword = state.currentPassword,
                newPassword = value
            )

            val confirmPasswordError = if (state.confirmNewPassword.isBlank()) {
                null
            } else {
                validateConfirmPassword(
                    newPassword = value,
                    confirmNewPassword = state.confirmNewPassword
                )
            }

            state.copy(
                newPassword = value,
                newPasswordError = newPasswordError,
                confirmPasswordError = confirmPasswordError,
                showInvalidDataDialog = false
            )
        }
    }

    fun onConfirmNewPasswordChange(value: String) {
        _uiState.update { state ->
            state.copy(
                confirmNewPassword = value,
                confirmPasswordError = validateConfirmPassword(
                    newPassword = state.newPassword,
                    confirmNewPassword = value
                ),
                showInvalidDataDialog = false
            )
        }
    }

    fun toggleCurrentPasswordVisibility() {
        _uiState.update {
            it.copy(
                currentPasswordVisible = !it.currentPasswordVisible
            )
        }
    }

    fun toggleNewPasswordVisibility() {
        _uiState.update {
            it.copy(
                newPasswordVisible = !it.newPasswordVisible
            )
        }
    }

    fun toggleConfirmPasswordVisibility() {
        _uiState.update {
            it.copy(
                confirmPasswordVisible = !it.confirmPasswordVisible
            )
        }
    }

    fun onApplyClick(userId: Long?) {
        currentPasswordCheckJob?.cancel()

        if (_uiState.value.hasVisibleErrors) {
            _uiState.update {
                it.copy(
                    showInvalidDataDialog = true
                )
            }
            return
        }

        val localFieldsAreValid = validateFields()

        if (!localFieldsAreValid) {
            _uiState.update {
                it.copy(
                    showInvalidDataDialog = true
                )
            }
            return
        }

        if (userId == null) {
            _uiState.update {
                it.copy(
                    showInvalidDataDialog = true
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isSaving = true,
                    showInvalidDataDialog = false
                )
            }

            val state = _uiState.value

            val currentPasswordIsVerified =
                state.currentPasswordStatus == CurrentPasswordStatus.VALID ||
                        verifyCurrentPasswordWithBackend(
                            userId = userId,
                            expectedPassword = state.currentPassword
                        )

            _uiState.update {
                it.copy(
                    isSaving = false
                )
            }

            if (!currentPasswordIsVerified) {
                _uiState.update {
                    it.copy(
                        showInvalidDataDialog = true
                    )
                }
                return@launch
            }

            _uiState.update {
                it.copy(
                    showSaveConfirmationDialog = true
                )
            }
        }
    }

    fun confirmSavePassword(userId: Long?) {
        if (userId == null) {
            _uiState.update {
                it.copy(
                    showSaveConfirmationDialog = false,
                    showInvalidDataDialog = true
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isSaving = true,
                    showSaveConfirmationDialog = false,
                    showInvalidDataDialog = false
                )
            }

            val state = _uiState.value

            try {
                val response = userRepository.updatePassword(
                    userId = userId,
                    request = UpdatePasswordRequest(
                        currentPassword = state.currentPassword,
                        newPassword = state.newPassword,
                        confirmNewPassword = state.confirmNewPassword
                    )
                )

                if (response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            currentPasswordStatus = CurrentPasswordStatus.VALID,
                            currentPasswordError = null,
                            newPasswordError = null,
                            confirmPasswordError = null,
                            showSuccessDialog = true
                        )
                    }
                } else {
                    val backendMessage = extractErrorMessage(
                        rawError = response.errorBody()?.string(),
                        fallback = "No se pudo actualizar la contraseña."
                    )

                    applyBackendPasswordError(backendMessage)

                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            showInvalidDataDialog = true
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        showInvalidDataDialog = true
                    )
                }
            }
        }
    }

    fun onCancelClick(): Boolean {
        val hasChanges = _uiState.value.hasChanges

        if (hasChanges) {
            _uiState.update {
                it.copy(
                    showCancelDialog = true
                )
            }
            return false
        }

        return true
    }

    fun onBackAttempt(): Boolean {
        return onCancelClick()
    }

    fun dismissInvalidDataDialog() {
        _uiState.update {
            it.copy(
                showInvalidDataDialog = false
            )
        }
    }

    fun dismissCancelDialog() {
        _uiState.update {
            it.copy(
                showCancelDialog = false
            )
        }
    }

    fun confirmCancel() {
        currentPasswordCheckJob?.cancel()
        rejectedCurrentPassword = ""

        _uiState.value = PasswordUiState()
    }

    fun confirmSuccess() {
        currentPasswordCheckJob?.cancel()
        rejectedCurrentPassword = ""

        _uiState.update {
            it.copy(
                showSuccessDialog = false,
                isSaving = false
            )
        }
    }

    private fun validateFields(): Boolean {
        val state = _uiState.value

        val currentLocalError = validateCurrentPassword(state.currentPassword)

        val currentError = when {
            currentLocalError != null -> currentLocalError
            state.currentPasswordStatus == CurrentPasswordStatus.INVALID -> {
                "La contraseña actual es incorrecta."
            }
            else -> null
        }

        val newPasswordError = validateNewPassword(
            currentPassword = state.currentPassword,
            newPassword = state.newPassword
        )

        val confirmPasswordError = validateConfirmPassword(
            newPassword = state.newPassword,
            confirmNewPassword = state.confirmNewPassword
        )

        _uiState.update {
            it.copy(
                currentPasswordError = currentError,
                newPasswordError = newPasswordError,
                confirmPasswordError = confirmPasswordError
            )
        }

        return currentError == null &&
                newPasswordError == null &&
                confirmPasswordError == null
    }

    private fun validateNewPassword(
        currentPassword: String,
        newPassword: String
    ): String? {
        val formatError = AccountValidators.validatePassword(
            password = newPassword,
            fieldName = "La nueva contraseña"
        )

        if (formatError != null) {
            return formatError
        }

        if (
            currentPassword.isNotBlank() &&
            newPassword == currentPassword
        ) {
            return "La nueva contraseña debe ser diferente a la anterior."
        }

        return null
    }

    private fun validateCurrentPassword(
        currentPassword: String
    ): String? {
        if (currentPassword.isBlank()) {
            return "La contraseña actual es obligatoria."
        }

        return null
    }

    private fun validateConfirmPassword(
        newPassword: String,
        confirmNewPassword: String
    ): String? {
        return AccountValidators.validateNewPasswordConfirmation(
            newPassword = newPassword,
            confirmNewPassword = confirmNewPassword
        )
    }

    private suspend fun verifyCurrentPasswordWithBackend(
        userId: Long,
        expectedPassword: String
    ): Boolean {
        return try {
            val response = userRepository.verifyCurrentPassword(
                userId = userId,
                currentPassword = expectedPassword
            )

            if (_uiState.value.currentPassword != expectedPassword) {
                return false
            }

            if (response.isSuccessful) {
                rejectedCurrentPassword = ""

                _uiState.update {
                    it.copy(
                        currentPasswordStatus = CurrentPasswordStatus.VALID,
                        currentPasswordError = null
                    )
                }

                true
            } else {
                rejectedCurrentPassword = expectedPassword

                _uiState.update {
                    it.copy(
                        currentPasswordStatus = CurrentPasswordStatus.INVALID,
                        currentPasswordError = "La contraseña actual es incorrecta."
                    )
                }

                false
            }
        } catch (e: Exception) {
            if (_uiState.value.currentPassword == expectedPassword) {
                _uiState.update {
                    it.copy(
                        currentPasswordStatus = CurrentPasswordStatus.IDLE,
                        currentPasswordError = "No se pudo verificar la contraseña."
                    )
                }
            }

            false
        }
    }

    private fun applyBackendPasswordError(message: String) {
        when {
            message.contains("actual", ignoreCase = true) -> {
                rejectedCurrentPassword = _uiState.value.currentPassword

                _uiState.update {
                    it.copy(
                        currentPasswordStatus = CurrentPasswordStatus.INVALID,
                        currentPasswordError = "La contraseña actual es incorrecta."
                    )
                }
            }

            message.contains("nueva contraseña", ignoreCase = true) -> {
                _uiState.update {
                    it.copy(
                        newPasswordError = message
                    )
                }
            }

            message.contains("confirmación", ignoreCase = true) ||
                    message.contains("no coinciden", ignoreCase = true) -> {
                _uiState.update {
                    it.copy(
                        confirmPasswordError = message
                    )
                }
            }

            else -> Unit
        }
    }

    private fun extractErrorMessage(
        rawError: String?,
        fallback: String
    ): String {
        val error = rawError.orEmpty()

        val messageFromJson = Regex("\"message\"\\s*:\\s*\"([^\"]+)\"")
            .find(error)
            ?.groupValues
            ?.getOrNull(1)

        return messageFromJson
            ?.replace("\\\"", "\"")
            ?: fallback
    }

    fun dismissSaveConfirmationDialog() {
        _uiState.update {
            it.copy(
                showSaveConfirmationDialog = false
            )
        }
    }
}