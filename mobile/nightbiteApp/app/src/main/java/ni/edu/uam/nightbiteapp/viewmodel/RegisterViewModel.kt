package ni.edu.uam.nightbiteapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import ni.edu.uam.nightbiteapp.data.remote.dto.MessageResponse
import ni.edu.uam.nightbiteapp.data.remote.dto.UserRegisterRequest
import ni.edu.uam.nightbiteapp.data.repository.UserRepository
import ni.edu.uam.nightbiteapp.ui.validation.AccountValidators

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

        val usernameError = AccountValidators.validateUsername(username)
        if (usernameError != null) {
            uiState = RegisterUiState.Error(usernameError)
            return
        }

        val emailError = AccountValidators.validateEmail(email)
        if (emailError != null) {
            uiState = RegisterUiState.Error(emailError)
            return
        }

        val passwordError = AccountValidators.validatePassword(password)
        if (passwordError != null) {
            uiState = RegisterUiState.Error(passwordError)
            return
        }

        val confirmPasswordError = AccountValidators.validateConfirmPassword(
            password = password,
            confirmPassword = confirmPassword
        )
        if (confirmPasswordError != null) {
            uiState = RegisterUiState.Error(confirmPasswordError)
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
                    username = username.trim().lowercase(),
                    email = email.trim().lowercase(),
                    password = password,
                    age = age
                )

                val response = userRepository.registerUser(request)

                if (
                    response.isSuccessful &&
                    response.body() != null
                ) {

                    uiState = RegisterUiState.Success(
                        "Cuenta creada correctamente."
                    )

                } else {

                    /**
                     * Si la API devuelve un error, intenta recuperar el mensaje
                     * enviado por el backend para mostrarlo al usuario.
                     */

                    val errorMessage = try {

                        val errorBody =
                            response.errorBody()?.string()

                        Gson().fromJson(
                            errorBody,
                            MessageResponse::class.java
                        )?.message

                    } catch (e: Exception) {
                        null
                    }

                    uiState = RegisterUiState.Error(
                        errorMessage
                            ?: "No se pudo crear la cuenta."
                    )
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