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

    val genderOptions = listOf(
        "Femenino",
        "Masculino"
    )

    fun onDriverNameChange(value: String) {
        _uiState.update { currentState ->
            currentState.copy(
                driverName = PlayerValidators.formatPersonName(value),
                errorMessage = null
            )
        }
    }

    fun onGenderSelected(value: String) {
        if (!genderOptions.contains(value)) {
            _uiState.update { currentState ->
                currentState.copy(
                    errorMessage = "El género seleccionado no es válido."
                )
            }
            return
        }

        _uiState.update { currentState ->
            currentState.copy(
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

        val normalizedDriverName = currentState.driverName.trim()
        val normalizedGender = currentState.gender.trim()

        val nameError = PlayerValidators.validateDriverName(normalizedDriverName)
        if (nameError != null) {
            showError(nameError)
            return
        }

        val genderError = PlayerValidators.validateGender(normalizedGender)
        if (genderError != null) {
            showError(genderError)
            return
        }

        viewModelScope.launch {
            try {
                _uiState.update { state ->
                    state.copy(
                        isLoading = true,
                        errorMessage = null
                    )
                }

                val session = sessionManager.userSessionFlow.first()
                val userId = session.userId

                if (userId == null) {
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            errorMessage = "No se encontró una sesión activa. Inicia sesión nuevamente."
                        )
                    }
                    return@launch
                }

                val playerRequest = PlayerRequest(
                    userAccountId = userId,
                    driverName = normalizedDriverName,
                    gender = normalizedGender,
                    helmetColor = PlayerValidators.DEFAULT_HELMET_COLOR,
                    motorcycleType = PlayerValidators.DEFAULT_MOTORCYCLE_TYPE
                )

                val response = playerRepository.createPlayer(playerRequest)

                if (response.isSuccessful && response.body() != null) {
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            isPlayerCreated = true,
                            errorMessage = null
                        )
                    }
                    return@launch
                }

                val errorBody = response.errorBody()?.string().orEmpty()

                if (
                    response.code() == 400 &&
                    errorBody.contains("ya tiene una ficha", ignoreCase = true)
                ) {
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            isPlayerCreated = true,
                            errorMessage = null
                        )
                    }
                    return@launch
                }

                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        errorMessage = "No se pudo completar la configuración de la cuenta."
                    )
                }
            } catch (e: Exception) {
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        errorMessage = "Error de conexión con la API."
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { currentState ->
            currentState.copy(
                errorMessage = null
            )
        }
    }

    private fun showError(message: String) {
        _uiState.update { currentState ->
            currentState.copy(
                errorMessage = message
            )
        }
    }
}