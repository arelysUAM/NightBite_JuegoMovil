package ni.edu.uam.nightbiteapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import ni.edu.uam.nightbiteapp.data.remote.dto.MessageResponse
import ni.edu.uam.nightbiteapp.data.remote.dto.UserLoginRequest
import ni.edu.uam.nightbiteapp.data.remote.dto.UserResponse
import ni.edu.uam.nightbiteapp.data.repository.UserRepository

/**
 * ViewModel encargado de manejar el inicio de sesión de cuentas de usuario.
 */
class LoginViewModel(
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    var uiState by mutableStateOf<LoginUiState>(LoginUiState.Idle)
        private set

    fun loginUser(
        usernameOrEmail: String,
        password: String
    ) {
        if (usernameOrEmail.isBlank() || password.isBlank()) {
            uiState = LoginUiState.Error(
                message = "Ingresa tu usuario o correo y contraseña.",
                type = LoginErrorType.IncompleteFields
            )
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

                if (
                    response.isSuccessful &&
                    response.body() != null
                ) {
                    val authResponse = response.body()!!

                    uiState = LoginUiState.Success(
                        user = authResponse.user,
                        token = authResponse.token
                    )
                } else {
                    val apiMessage = try {
                        val errorBody = response.errorBody()?.string()

                        Gson().fromJson(
                            errorBody,
                            MessageResponse::class.java
                        )?.message
                    } catch (e: Exception) {
                        null
                    }

                    uiState = buildLoginError(apiMessage)
                }
            } catch (e: Exception) {
                uiState = LoginUiState.Error(
                    message = "No se pudo conectar con el servidor. Inténtalo nuevamente.",
                    type = LoginErrorType.ConnectionError
                )
            }
        }
    }

    private fun buildLoginError(
        apiMessage: String?
    ): LoginUiState.Error {
        val cleanMessage = apiMessage.orEmpty().trim()
        val normalizedMessage = cleanMessage.lowercase()

        return when {
            normalizedMessage.contains("usuario no encontrado") ||
                    normalizedMessage.contains("cuenta no encontrada") -> {
                LoginUiState.Error(
                    message = "Usuario no encontrado. Puedes crear una cuenta para continuar.",
                    type = LoginErrorType.UserNotFound
                )
            }

            normalizedMessage.contains("campos incompletos") ||
                    normalizedMessage.contains("ingresa") -> {
                LoginUiState.Error(
                    message = "Ingresa tu usuario o correo y contraseña.",
                    type = LoginErrorType.IncompleteFields
                )
            }

            normalizedMessage.contains("contraseña") ||
                    normalizedMessage.contains("credenciales") ||
                    normalizedMessage.contains("incorrectos") ||
                    normalizedMessage.contains("inválidas") -> {
                LoginUiState.Error(
                    message = "Usuario o contraseña incorrectos. Vuelve a intentarlo.",
                    type = LoginErrorType.InvalidCredentials
                )
            }

            else -> {
                LoginUiState.Error(
                    message = "Usuario o contraseña incorrectos. Vuelve a intentarlo.",
                    type = LoginErrorType.InvalidCredentials
                )
            }
        }
    }

    fun resetState() {
        uiState = LoginUiState.Idle
    }
}