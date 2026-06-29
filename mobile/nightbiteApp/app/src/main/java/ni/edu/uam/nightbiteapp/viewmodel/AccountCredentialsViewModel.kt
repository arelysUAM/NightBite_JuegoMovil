package ni.edu.uam.nightbiteapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ni.edu.uam.nightbiteapp.data.local.session.SessionManager
import ni.edu.uam.nightbiteapp.data.remote.dto.UpdateAccountInfoRequest
import ni.edu.uam.nightbiteapp.data.remote.dto.UpdatePasswordRequest
import ni.edu.uam.nightbiteapp.data.remote.dto.UpdateUsernameRequest
import ni.edu.uam.nightbiteapp.data.repository.UserRepository
import ni.edu.uam.nightbiteapp.ui.validation.AccountValidators
import ni.edu.uam.nightbiteapp.ui.validation.PlayerValidators
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import ni.edu.uam.nightbiteapp.data.repository.ProgressSyncRepository
import kotlinx.coroutines.flow.first
import ni.edu.uam.nightbiteapp.data.local.session.UserSession

class AccountCredentialsViewModel(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager,
    private val progressSyncRepository: ProgressSyncRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountCredentialsUiState())
    private var usernameAvailabilityJob: Job? = null
    val uiState: StateFlow<AccountCredentialsUiState> = _uiState.asStateFlow()

    val genderOptions = listOf("Femenino", "Masculino")

    fun loadAccountInfo(userId: Long?) {
        if (userId == null || userId == 0L) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = "No se pudo identificar al usuario activo."
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            val localSession = sessionManager.userSessionFlow.first()

            if (localSession.isLoggedIn && localSession.userId == userId) {
                applyLocalSessionToState(localSession)
            }

            try {
                val response = userRepository.getUserById(userId)

                if (!response.isSuccessful || response.body() == null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = if (it.username.isBlank() && it.email.isBlank()) {
                                "No se pudo cargar la información de la cuenta."
                            } else {
                                null
                            }
                        )
                    }
                    return@launch
                }

                val user = response.body()!!
                val username = user.username
                val age = user.age.toString()
                val gender = user.player?.gender.orEmpty()

                _uiState.update {
                    it.copy(
                        username = username,
                        email = user.email,
                        age = age,
                        gender = gender,
                        createdAt = formatCreatedAt(user.createdAt),

                        originalUsername = username,
                        originalAge = age,
                        originalGender = gender,

                        usernameError = null,
                        ageError = null,
                        genderError = null,

                        isEditing = false,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = if (it.username.isBlank() && it.email.isBlank()) {
                            "Error de conexión con la API."
                        } else {
                            null
                        }
                    )
                }
            }
        }
    }

    fun onUsernameChange(value: String) {
        val cleanedUsername = value
            .trim()
            .lowercase()
            .filter { character ->
                character.isLetterOrDigit() || character == '_'
            }
            .take(AccountValidators.USERNAME_MAX_LENGTH)

        val formatError = AccountValidators.validateUsername(cleanedUsername)

        _uiState.update {
            it.copy(
                username = cleanedUsername,
                usernameError = formatError,
                errorMessage = null
            )
        }

        usernameAvailabilityJob?.cancel()

        if (
            cleanedUsername.isBlank() ||
            formatError != null
        ) {
            return
        }

        if (cleanedUsername == _uiState.value.originalUsername) {
            _uiState.update {
                it.copy(usernameError = null)
            }
            return
        }

        usernameAvailabilityJob = viewModelScope.launch {
            delay(500L)

            try {
                val response = userRepository.checkUsernameAvailability(cleanedUsername)

                val currentUsername = _uiState.value.username

                if (currentUsername != cleanedUsername) {
                    return@launch
                }

                if (!response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            usernameError = "No se pudo verificar el usuario."
                        )
                    }
                    return@launch
                }

                val availability = response.body()

                if (availability?.available == false) {
                    _uiState.update {
                        it.copy(
                            usernameError = availability.message
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            usernameError = null
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        usernameError = "No se pudo verificar el usuario."
                    )
                }
            }
        }
    }

    fun onAgeChange(value: String) {
        val cleanedAge = value
            .filter { it.isDigit() }
            .take(3)

        _uiState.update {
            it.copy(
                age = cleanedAge,
                ageError = validateAge(cleanedAge),
                errorMessage = null
            )
        }
    }

    fun onGenderSelected(value: String) {
        _uiState.update {
            it.copy(
                gender = value,
                genderError = null,
                errorMessage = null
            )
        }
    }

    fun beginEditing() {
        _uiState.update {
            it.copy(
                isEditing = true,
                usernameError = null,
                ageError = null,
                genderError = null,
                errorMessage = null
            )
        }
    }

    private fun applyLocalSessionToState(
        session: UserSession
    ) {
        val localUsername = session.username
        val localEmail = session.email
        val localAge = session.age?.toString().orEmpty()
        val localGender = session.playerGender

        _uiState.update {
            it.copy(
                username = localUsername,
                email = localEmail,
                age = localAge,
                gender = localGender,
                createdAt = "No disponible sin conexión",

                originalUsername = localUsername,
                originalAge = localAge,
                originalGender = localGender,

                usernameError = null,
                ageError = null,
                genderError = null,

                isEditing = false,
                isLoading = false,
                errorMessage = null
            )
        }
    }

    fun onSaveChangesClick() {
        val isValid = validateAccountInfoFields()

        if (!isValid) {
            _uiState.update {
                it.copy(
                    showInvalidDataDialog = true,
                    errorMessage = null
                )
            }
            return
        }

        if (!hasEditableChanges()) {
            _uiState.update {
                it.copy(
                    showInvalidDataDialog = true,
                    errorMessage = null
                )
            }
            return
        }

        _uiState.update {
            it.copy(
                showSaveConfirmationDialog = true,
                errorMessage = null
            )
        }
    }

    fun dismissInvalidDataDialog() {
        _uiState.update {
            it.copy(
                showInvalidDataDialog = false
            )
        }
    }

    fun dismissSaveConfirmationDialog() {
        _uiState.update {
            it.copy(
                showSaveConfirmationDialog = false
            )
        }
    }

    fun confirmSaveChanges(userId: Long?) {
        if (userId == null) {
            _uiState.update {
                it.copy(
                    showSaveConfirmationDialog = false,
                    errorMessage = "No se pudo identificar al usuario activo."
                )
            }
            return
        }

        val state = _uiState.value
        val ageValue = state.age.toIntOrNull()

        if (ageValue == null) {
            _uiState.update {
                it.copy(
                    showSaveConfirmationDialog = false,
                    ageError = "La edad es obligatoria."
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    showSaveConfirmationDialog = false,
                    errorMessage = null
                )
            }

            try {
                val usernameChanged =
                    state.username.trim().lowercase() != state.originalUsername

                if (usernameChanged) {
                    val availabilityResponse =
                        userRepository.checkUsernameAvailability(state.username)

                    if (!availabilityResponse.isSuccessful) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                usernameError = "No se pudo verificar el nombre de usuario."
                            )
                        }
                        return@launch
                    }



                    val availability = availabilityResponse.body()

                    if (availability?.available == false) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                usernameError = availability.message
                            )
                        }
                        return@launch
                    }
                }

                val response = userRepository.updateAccountInfo(
                    userId = userId,
                    request = UpdateAccountInfoRequest(
                        username = state.username.trim().lowercase(),
                        age = ageValue,
                        gender = state.gender
                    )
                )

                if (!response.isSuccessful) {
                    val errorMessage = extractErrorMessage(
                        rawError = response.errorBody()?.string(),
                        fallback = "No se pudo actualizar la información de la cuenta."
                    )

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = errorMessage
                        )
                    }
                    return@launch
                }

                val updatedUser = response.body()

                val updatedUsername = updatedUser
                    ?.username
                    ?: state.username.trim().lowercase()

                val updatedAge = updatedUser
                    ?.age
                    ?.toString()
                    ?: ageValue.toString()

                val updatedGender = updatedUser
                    ?.player
                    ?.gender
                    ?: state.gender

                _uiState.update {
                    it.copy(
                        username = updatedUsername,
                        age = updatedAge,
                        gender = updatedGender,

                        originalUsername = updatedUsername,
                        originalAge = updatedAge,
                        originalGender = updatedGender,

                        usernameError = null,
                        ageError = null,
                        genderError = null,

                        isLoading = false,
                        isEditing = false,
                        showChangesSavedDialog = true,
                        successMessage = "Información actualizada correctamente."
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error de conexión con la API."
                    )
                }
            }
        }
    }

    fun dismissChangesSavedDialog() {
        _uiState.update {
            it.copy(
                showChangesSavedDialog = false,
                successMessage = null,
                errorMessage = null
            )
        }
    }
    fun onCancelEditingClick() {
        _uiState.update {
            it.copy(
                showCancelConfirmationDialog = true,
                errorMessage = null
            )
        }
    }

    fun dismissCancelConfirmationDialog() {
        _uiState.update {
            it.copy(
                showCancelConfirmationDialog = false
            )
        }
    }

    fun confirmCancelEditing() {
        restoreOriginalValuesAndLock()
    }

    fun onBackAttempt(onBackToSettings: () -> Unit) {
        val state = _uiState.value

        if (state.isEditing) {
            _uiState.update {
                it.copy(
                    showExitConfirmationDialog = true,
                    errorMessage = null
                )
            }
        } else {
            onBackToSettings()
        }
    }

    fun dismissExitConfirmationDialog() {
        _uiState.update {
            it.copy(
                showExitConfirmationDialog = false
            )
        }
    }

    fun confirmExitWithoutSaving(onBackToSettings: () -> Unit) {
        restoreOriginalValuesAndLock()
        onBackToSettings()
    }

    fun onResetProgressClick() {
        _uiState.update {
            it.copy(
                showResetProgressDialog = true
            )
        }
    }

    fun dismissResetProgressDialog() {
        _uiState.update {
            it.copy(
                showResetProgressDialog = false
            )
        }
    }

    fun confirmResetProgress(userId: Long?) {
        if (userId == null || userId == 0L) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    showResetProgressDialog = false,
                    showProgressResetSuccessDialog = false,
                    errorMessage = "No se pudo identificar al usuario activo."
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    showResetProgressDialog = false,
                    showProgressResetSuccessDialog = false,
                    errorMessage = null,
                    successMessage = null
                )
            }

            val resetSuccessful = progressSyncRepository.resetProgress(userId)

            if (!resetSuccessful) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "No se pudo reiniciar el progreso."
                    )
                }
                return@launch
            }

            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = null,
                    successMessage = "Progreso reiniciado correctamente.",
                    showProgressResetSuccessDialog = true
                )
            }
        }
    }

    fun finishResetProgressFlow(
        onNavigateToHome: () -> Unit
    ) {
        _uiState.update {
            it.copy(
                showProgressResetSuccessDialog = false,
                successMessage = null,
                errorMessage = null
            )
        }
        onNavigateToHome()
    }

    private fun validateAccountInfoFields(): Boolean {
        val state = _uiState.value

        val formatUsernameError = AccountValidators.validateUsername(state.username)

        val usernameError = when {
            formatUsernameError != null -> formatUsernameError
            state.usernameError != null -> state.usernameError
            else -> null
        }

        val ageError = validateAge(state.age)
        val genderError = PlayerValidators.validateGender(state.gender)

        _uiState.update {
            it.copy(
                usernameError = usernameError,
                ageError = ageError,
                genderError = genderError,
                errorMessage = null
            )
        }

        return usernameError == null &&
                ageError == null &&
                genderError == null
    }

    private fun validateAge(ageText: String): String? {
        if (ageText.isBlank()) {
            return "La edad es obligatoria."
        }

        val age = ageText.toIntOrNull()
            ?: return "La edad solo puede contener números."

        if (age < 13) {
            return "Debes tener 13 años o más."
        }

        if (age > 120) {
            return "La edad no puede ser mayor a 120 años."
        }

        return null
    }

    private fun restoreOriginalValuesAndLock() {
        _uiState.update {
            it.copy(
                username = it.originalUsername,
                age = it.originalAge,
                gender = it.originalGender,

                usernameError = null,
                ageError = null,
                genderError = null,

                isEditing = false,
                showCancelConfirmationDialog = false,
                showExitConfirmationDialog = false,
                errorMessage = null
            )
        }
    }

    private fun hasEditableChanges(): Boolean {
        val state = _uiState.value

        return state.username != state.originalUsername ||
                state.age != state.originalAge ||
                state.gender != state.originalGender
    }

    private fun formatCreatedAt(createdAt: String?): String {
        if (createdAt.isNullOrBlank()) {
            return "No disponible"
        }

        val date = createdAt.take(10)

        if (!date.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) {
            return date
        }

        val year = date.substring(0, 4)
        val month = date.substring(5, 7)
        val day = date.substring(8, 10)

        return "$day/$month/$year"
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

    /*
     * Funciones antiguas de credenciales.
     * Se conservan por compatibilidad mientras reemplazamos AccountScreen.
     */

    fun onNewUsernameChange(value: String) {
        _uiState.update {
            it.copy(
                newUsername = value.trim().lowercase(),
                errorMessage = null
            )
        }
    }

    fun onCurrentPasswordChange(value: String) {
        _uiState.update {
            it.copy(
                currentPassword = value,
                errorMessage = null
            )
        }
    }

    fun onNewPasswordChange(value: String) {
        _uiState.update {
            it.copy(
                newPassword = value,
                errorMessage = null
            )
        }
    }

    fun onConfirmNewPasswordChange(value: String) {
        _uiState.update {
            it.copy(
                confirmNewPassword = value,
                errorMessage = null
            )
        }
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
                _uiState.update {
                    it.copy(
                        errorMessage = usernameError
                    )
                }
                return
            }
        }

        if (state.newUsername.isNotBlank() && state.newUsername == currentUsername) {
            _uiState.update {
                it.copy(
                    errorMessage = "El nuevo nombre de usuario debe ser diferente al actual."
                )
            }
            return
        }

        if (wantsToChangePassword) {
            if (
                state.currentPassword.isBlank() ||
                state.newPassword.isBlank() ||
                state.confirmNewPassword.isBlank()
            ) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Para cambiar la contraseña debes completar todos los campos de contraseña."
                    )
                }
                return
            }

            val currentPasswordError = AccountValidators.validatePassword(
                password = state.currentPassword,
                fieldName = "La contraseña actual"
            )

            if (currentPasswordError != null) {
                _uiState.update {
                    it.copy(
                        errorMessage = currentPasswordError
                    )
                }
                return
            }

            val newPasswordError = AccountValidators.validatePassword(
                password = state.newPassword,
                fieldName = "La nueva contraseña"
            )

            if (newPasswordError != null) {
                _uiState.update {
                    it.copy(
                        errorMessage = newPasswordError
                    )
                }
                return
            }

            val confirmPasswordError = AccountValidators.validateNewPasswordConfirmation(
                newPassword = state.newPassword,
                confirmNewPassword = state.confirmNewPassword
            )

            if (confirmPasswordError != null) {
                _uiState.update {
                    it.copy(
                        errorMessage = confirmPasswordError
                    )
                }
                return
            }

            if (state.currentPassword == state.newPassword) {
                _uiState.update {
                    it.copy(
                        errorMessage = "La nueva contraseña debe ser diferente a la actual."
                    )
                }
                return
            }
        }

        _uiState.update {
            it.copy(
                showConfirmDialog = true,
                errorMessage = null
            )
        }
    }

    fun dismissConfirmDialog() {
        _uiState.update {
            it.copy(
                showConfirmDialog = false
            )
        }
    }

    fun applyConfirmedChanges(
        userId: Long?,
        currentUsername: String
    ) {
        if (userId == null) {
            _uiState.update {
                it.copy(
                    showConfirmDialog = false,
                    errorMessage = "No se pudo identificar al usuario activo."
                )
            }
            return
        }

        viewModelScope.launch {
            val state = _uiState.value

            _uiState.update {
                it.copy(
                    isLoading = true,
                    showConfirmDialog = false,
                    errorMessage = null
                )
            }

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
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = "No se pudo actualizar el nombre de usuario."
                            )
                        }
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
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = "No se pudo actualizar la contraseña. Verifica la contraseña actual."
                            )
                        }
                        return@launch
                    }
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        showCredentialsUpdatedDialog = true,
                        successMessage = "Credenciales actualizadas correctamente."
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error de conexión con el servidor."
                    )
                }
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
        _uiState.update {
            it.copy(
                showDeleteAccountDialog = true,
                errorMessage = null
            )
        }
    }

    fun dismissDeleteAccountDialog() {
        _uiState.update {
            it.copy(
                showDeleteAccountDialog = false
            )
        }
    }

    fun deleteAccount(userId: Long?) {
        if (userId == null || userId == 0L) {
            _uiState.update {
                it.copy(
                    errorMessage = "No se pudo identificar la cuenta del usuario.",
                    showDeleteAccountDialog = false
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                    showDeleteAccountDialog = false
                )
            }

            try {
                val response = userRepository.deleteUser(userId)

                if (!response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "No se pudo eliminar la cuenta."
                        )
                    }
                    return@launch
                }

                try {
                    progressSyncRepository.clearLocalProgressForDeletedAccount(userId)
                } catch (_: Exception) {
                    // La cuenta ya fue eliminada en la API.
                    // Si la limpieza local falla por alguna razón, no bloqueamos el flujo.
                }

                sessionManager.clearSession()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        showAccountDeletedDialog = true
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error de conexión con la API."
                    )
                }
            }
        }
    }

    fun finishAccountDeletedFlow(onNavigateToLogin: () -> Unit) {
        _uiState.value = AccountCredentialsUiState()
        onNavigateToLogin()
    }
}