package ni.edu.uam.nightbiteapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ni.edu.uam.nightbiteapp.data.remote.dto.UserRegisterRequest
import ni.edu.uam.nightbiteapp.data.repository.UserRepository

/**
 * ViewModel encargado de manejar la lógica de registro de cuentas de usuario.
 *
 * Recibe los datos desde la pantalla de registro, realiza validaciones básicas
 * y se comunica con el repositorio para enviar la información hacia la API.
 *
 * Importante:
 * UserAccount representa la cuenta real del jugador.
 * Player se utilizará más adelante para el personaje/avatar dentro del juego.
 */
class RegisterViewModel(
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    var uiState by mutableStateOf<RegisterUiState>(RegisterUiState.Idle)
        private set

    /**
     * Registra una nueva cuenta de usuario.
     *
     * La contraseña se envía al backend para que sea cifrada con BCrypt.
     * Android no guarda ni procesa el hash de la contraseña.
     */
    fun registerUser(
        username: String,
        email: String,
        password: String,
        confirmPassword: String,
        age: Int
    ) {
        if (username.isBlank()) {
            uiState = RegisterUiState.Error("El nombre de usuario es obligatorio.")
            return
        }

        if (email.isBlank()) {
            uiState = RegisterUiState.Error("El correo es obligatorio.")
            return
        }

        if (password.isBlank()) {
            uiState = RegisterUiState.Error("La contraseña es obligatoria.")
            return
        }

        if (password.length < 6) {
            uiState = RegisterUiState.Error("La contraseña debe tener al menos 6 caracteres.")
            return
        }

        if (password != confirmPassword) {
            uiState = RegisterUiState.Error("Las contraseñas no coinciden.")
            return
        }

        if (age < 13) {
            uiState = RegisterUiState.Error("Debes tener 13 años o más para crear una cuenta.")
            return
        }

        uiState = RegisterUiState.Loading

        viewModelScope.launch {
            try {
                val request = UserRegisterRequest(
                    username = username.trim(),
                    email = email.trim(),
                    password = password,
                    age = age
                )

                val response = userRepository.registerUser(request)

                uiState = if (response.isSuccessful && response.body() != null) {
                    RegisterUiState.Success("Cuenta creada correctamente.")
                } else {
                    RegisterUiState.Error("No se pudo crear la cuenta.")
                }
            } catch (e: Exception) {
                uiState = RegisterUiState.Error("Error de conexión con la API.")
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