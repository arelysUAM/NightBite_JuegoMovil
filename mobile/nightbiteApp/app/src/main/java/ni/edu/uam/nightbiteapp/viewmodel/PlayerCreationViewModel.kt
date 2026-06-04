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

class PlayerCreationViewModel(
    private val sessionManager: SessionManager,
    private val playerRepository: PlayerRepository = PlayerRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlayerCreationUiState())
    val uiState: StateFlow<PlayerCreationUiState> = _uiState

    val genderOptions = listOf("Femenino", "Masculino")
    val helmetColorOptions = listOf("Negro", "Rojo", "Azul", "Blanco", "Amarillo")
    val motorcycleTypeOptions = listOf("Estándar", "Scooter", "Deportiva", "Retro", "Delivery")

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
                driverName = value,
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

        if (currentState.nickname.isBlank()) {
            showError("Ingresa el apodo del repartidor.")
            return
        }

        if (currentState.driverName.isBlank()) {
            showError("Ingresa el nombre del repartidor.")
            return
        }

        if (currentState.gender.isBlank()) {
            showError("Selecciona el género.")
            return
        }

        if (currentState.helmetColor.isBlank()) {
            showError("Selecciona el color del casco.")
            return
        }

        if (currentState.motorcycleType.isBlank()) {
            showError("Selecciona el tipo de moto.")
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
                    nickname = currentState.nickname.trim(),
                    driverName = currentState.driverName.trim(),
                    gender = currentState.gender,
                    helmetColor = currentState.helmetColor,
                    motorcycleType = currentState.motorcycleType
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
}