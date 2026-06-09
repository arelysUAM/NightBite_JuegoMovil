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
import ni.edu.uam.nightbiteapp.ui.validation.PlayerValidators.formatPersonName

class PlayerCreationViewModel(
    private val sessionManager: SessionManager,
    private val playerRepository: PlayerRepository = PlayerRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlayerCreationUiState())
    val uiState: StateFlow<PlayerCreationUiState> = _uiState

    val genderOptions = listOf("Femenino", "Masculino")
    val helmetColorOptions = PlayerValidators.ALLOWED_HELMET_COLORS
    val motorcycleTypeOptions = PlayerValidators.ALLOWED_MOTORCYCLE_TYPES

    fun onNicknameChange(value: String) {
        _uiState.update {
            it.copy(
                nickname = value,
                errorMessage = null
            )
        }
    }

    fun onDriverNameChange(value: String) {
        _uiState.update {
            it.copy(
                driverName = formatSingleName(value),
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

    fun onHelmetColorSelected(value: String) {
        _uiState.update {
            it.copy(
                helmetColor = value,
                errorMessage = null
            )
        }
    }

    fun onMotorcycleTypeSelected(value: String) {
        _uiState.update {
            it.copy(
                motorcycleType = value,
                errorMessage = null
            )
        }
    }

    fun createPlayer() {
        val currentState = _uiState.value

        val nicknameError = PlayerValidators.validateNickname(currentState.nickname)
        if (nicknameError != null) {
            showError(nicknameError)
            return
        }

        val driverNameError = PlayerValidators.validateDriverName(currentState.driverName)
        if (driverNameError != null) {
            showError(driverNameError)
            return
        }

        val genderError = PlayerValidators.validateGender(currentState.gender)
        if (genderError != null) {
            showError(genderError)
            return
        }

        val helmetColorError = PlayerValidators.validateHelmetColor(currentState.helmetColor)
        if (helmetColorError != null) {
            showError(helmetColorError)
            return
        }

        val motorcycleTypeError = PlayerValidators.validateMotorcycleType(currentState.motorcycleType)
        if (motorcycleTypeError != null) {
            showError(motorcycleTypeError)
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
                    nickname = currentState.nickname.trim().lowercase(),
                    driverName = currentState.driverName.trim(),
                    gender = currentState.gender,
                    helmetColor = currentState.helmetColor.trim(),
                    motorcycleType = currentState.motorcycleType.trim()
                )

                val response = playerRepository.createPlayer(playerRequest)

                if (response.isSuccessful && response.body() != null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isPlayerCreated = true
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "No se pudo crear la ficha. Verifica si este usuario ya tiene un Player."
                        )
                    }
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

    private fun formatSingleName(value: String): String {
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
}