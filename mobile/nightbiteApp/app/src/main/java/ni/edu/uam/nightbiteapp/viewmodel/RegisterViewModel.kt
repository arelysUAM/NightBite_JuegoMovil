package ni.edu.uam.nightbiteapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ni.edu.uam.nightbiteapp.data.remote.dto.PlayerRequest
import ni.edu.uam.nightbiteapp.data.repository.PlayerRepository

/**
 * ViewModel encargado de manejar la lógica de registro de jugadores.
 *
 * Recibe los datos desde la pantalla de registro, realiza validaciones
 * básicas y se comunica con el repositorio para enviar la información
 * hacia la API.
 */
class RegisterViewModel(
    private val playerRepository: PlayerRepository = PlayerRepository()
) : ViewModel() {

    var uiState by mutableStateOf<RegisterUiState>(RegisterUiState.Idle)
        private set

    /**
     * Registra un nuevo jugador usando los datos ingresados en pantalla.
     *
     * En esta primera versión se envían username y email, porque son los
     * campos que actualmente maneja el endpoint de Player en el backend.
     */
    fun registerPlayer(
        username: String,
        email: String
    ) {
        if (username.isBlank() || email.isBlank()) {
            uiState = RegisterUiState.Error(
                message = "Completa el nombre de jugador y el correo."
            )
            return
        }

        uiState = RegisterUiState.Loading

        viewModelScope.launch {
            try {
                val request = PlayerRequest(
                    username = username.trim(),
                    email = email.trim()
                )

                val response = playerRepository.createPlayer(request)

                uiState = if (response.isSuccessful && response.body() != null) {
                    RegisterUiState.Success(
                        message = "Jugador registrado correctamente."
                    )
                } else {
                    RegisterUiState.Error(
                        message = "No se pudo registrar el jugador."
                    )
                }
            } catch (e: Exception) {
                uiState = RegisterUiState.Error(
                    message = "Error de conexión con la API."
                )
            }
        }
    }

    /**
     * Reinicia el estado de la pantalla.
     */
    fun resetState() {
        uiState = RegisterUiState.Idle
    }
}