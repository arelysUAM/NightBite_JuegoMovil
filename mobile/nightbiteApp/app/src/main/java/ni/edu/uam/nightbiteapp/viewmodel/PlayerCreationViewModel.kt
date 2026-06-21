package ni.edu.uam.nightbiteapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ni.edu.uam.nightbiteapp.data.local.session.SessionManager
import ni.edu.uam.nightbiteapp.data.remote.dto.PlayerRequest
import ni.edu.uam.nightbiteapp.data.repository.PlayerRepository
import ni.edu.uam.nightbiteapp.ui.validation.PlayerValidators

class PlayerCreationViewModel(
    private val sessionManager: SessionManager,
    private val playerRepository: PlayerRepository = PlayerRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlayerCreationUiState())
    val uiState: StateFlow<PlayerCreationUiState> = _uiState

    val genderOptions = listOf("Femenino", "Masculino")

    fun onDriverNameChange(value: String) {
        _uiState.update {
            it.copy(
                driverName = PlayerValidators.formatPersonName(value),
                errorMessage = null
            )
        }
    }

    fun onGenderSelected(value: String) {
        _uiState.update {
            it.copy(
                gender = value,
                errorMessage = null
            )
        }
    }

    fun createPlayer() {
        val currentState = _uiState.value

        if (currentState.isLoading) {
            return
        }

        val nameError = PlayerValidators.validateDriverName(currentState.driverName)

        if (nameError != null) {
            showError(nameError)
            return
        }

        val genderError = PlayerValidators.validateGender(currentState.gender)
        if (genderError != null) {
            showError(genderError)
            return
        }

        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(
                        isLoading = true,
                        errorMessage = null
                    )
                }

                val session = sessionManager.userSessionFlow.first()
                val userId = session.userId

                if (userId == null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "No se encontró una sesión activa. Inicia sesión nuevamente."
                        )
                    }
                    return@launch
                }

                val playerRequest = PlayerRequest(
                    userAccountId = userId,
                    driverName = currentState.driverName.trim(),
                    gender = currentState.gender,
                    helmetColor = PlayerValidators.DEFAULT_HELMET_COLOR,
                    motorcycleType = PlayerValidators.DEFAULT_MOTORCYCLE_TYPE
                )

                val response = playerRepository.createPlayer(playerRequest)

                if (response.isSuccessful && response.body() != null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isPlayerCreated = true
                        )
                    }
                    return@launch
                }

                val errorBody = response.errorBody()?.string().orEmpty()

                if (
                    response.code() == 400 &&
                    errorBody.contains("ya tiene una ficha", ignoreCase = true)
                ) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isPlayerCreated = true
                        )
                    }
                    return@launch
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "No se pudo completar la configuración de la cuenta."
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

    fun clearError() {
        _uiState.update {
            it.copy(errorMessage = null)
        }
    }

    private fun showError(message: String) {
        _uiState.update {
            it.copy(errorMessage = message)
        }
    }

    private fun validateDriverName(value: String): String? {
        val cleanedValue = value.trim()

        if (cleanedValue.isBlank()) {
            return "Ingresa tu nombre para continuar."
        }

        if (cleanedValue.length > 80) {
            return "El nombre no debe superar los 80 caracteres."
        }

        if (!cleanedValue.matches(Regex("^[A-Za-zÁÉÍÓÚáéíóúÑñ]+$"))) {
            return "El nombre solo puede contener letras."
        }

        return null
    }

    private fun formatDriverName(value: String): String {
        val cleanedValue = value
            .replace(" ", "")
            .lowercase()

        return cleanedValue.replaceFirstChar { char ->
            if (char.isLowerCase()) {
                char.titlecase()
            } else {
                char.toString()
            }
        }
    }

    companion object {
        private const val DEFAULT_HELMET_COLOR = "Negro"
        private const val DEFAULT_MOTORCYCLE_TYPE = "Delivery"
    }
}