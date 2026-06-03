package ni.edu.uam.nightbiteapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ni.edu.uam.nightbiteapp.data.remote.dto.UserLoginRequest
import ni.edu.uam.nightbiteapp.data.repository.UserRepository

/**
 * ViewModel encargado de manejar el inicio de sesión de cuentas de usuario.
 *
 * Valida los campos ingresados y se comunica con la API para comprobar
 * que las credenciales pertenezcan a una cuenta registrada.
 */
class LoginViewModel(
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    var uiState by mutableStateOf<LoginUiState>(LoginUiState.Idle)
        private set

    /**
     * Inicia sesión usando usuario/correo y contraseña.
     */
    fun loginUser(
        usernameOrEmail: String,
        password: String
    ) {
        if (usernameOrEmail.isBlank()) {
            uiState = LoginUiState.Error("Ingresa tu usuario o correo.")
            return
        }

        if (password.isBlank()) {
            uiState = LoginUiState.Error("Ingresa tu contraseña.")
            return
        }

        uiState = LoginUiState.Loading

        viewModelScope.launch {
            try {
                val request = UserLoginRequest(
                    usernameOrEmail = usernameOrEmail.trim(),
                    password = password
                )

                val response = userRepository.loginUser(request)

                uiState = if (response.isSuccessful && response.body() != null) {
                    LoginUiState.Success(response.body()!!)
                } else {
                    LoginUiState.Error("Credenciales inválidas.")
                }
            } catch (e: Exception) {
                uiState = LoginUiState.Error("Error de conexión con la API.")
            }
        }
    }

    /**
     * Reinicia el estado después de mostrar un mensaje o navegar.
     */
    fun resetState() {
        uiState = LoginUiState.Idle
    }
}