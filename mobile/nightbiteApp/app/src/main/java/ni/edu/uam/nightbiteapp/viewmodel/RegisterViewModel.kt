package ni.edu.uam.nightbiteapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ni.edu.uam.nightbiteapp.data.remote.dto.MessageResponse
import ni.edu.uam.nightbiteapp.data.remote.dto.UserRegisterRequest
import ni.edu.uam.nightbiteapp.data.repository.UserRepository
import ni.edu.uam.nightbiteapp.ui.validation.AccountValidators

class RegisterViewModel(
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    var uiState by mutableStateOf<RegisterUiState>(RegisterUiState.Idle)
        private set

    var usernameAvailabilityState by mutableStateOf<UsernameAvailabilityUiState>(
        UsernameAvailabilityUiState.Idle
    )
        private set

    private var usernameCheckJob: Job? = null

    fun checkUsernameAvailability(username: String) {
        usernameCheckJob?.cancel()

        val normalizedUsername = username.trim().lowercase()

        val usernameFormatError = AccountValidators.validateUsername(normalizedUsername)
        if (usernameFormatError != null) {
            usernameAvailabilityState = UsernameAvailabilityUiState.Idle
            return
        }

        usernameCheckJob = viewModelScope.launch {
            delay(USERNAME_CHECK_DELAY)

            usernameAvailabilityState = UsernameAvailabilityUiState.Checking

            try {
                val response = userRepository.checkUsernameAvailability(normalizedUsername)

                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!

                    usernameAvailabilityState = if (body.available) {
                        UsernameAvailabilityUiState.Available(
                            message = body.message
                        )
                    } else {
                        UsernameAvailabilityUiState.Unavailable(
                            message = body.message
                        )
                    }
                } else {
                    val errorMessage = extractErrorMessage(
                        errorBody = response.errorBody()?.string()
                    )

                    usernameAvailabilityState = UsernameAvailabilityUiState.Error(
                        message = errorMessage ?: "No se pudo verificar el usuario."
                    )
                }
            } catch (e: Exception) {
                usernameAvailabilityState = UsernameAvailabilityUiState.Error(
                    message = "No se pudo verificar el usuario."
                )
            }
        }
    }

    fun clearUsernameAvailability() {
        usernameCheckJob?.cancel()
        usernameAvailabilityState = UsernameAvailabilityUiState.Idle
    }

    fun registerUser(
        username: String,
        email: String,
        password: String,
        confirmPassword: String,
        age: Int
    ) {
        val normalizedUsername = username.trim().lowercase()
        val normalizedEmail = email.trim().lowercase()

        val usernameError = AccountValidators.validateUsername(normalizedUsername)
        if (usernameError != null) {
            uiState = RegisterUiState.Error(usernameError)
            return
        }

        val currentAvailabilityState = usernameAvailabilityState
        if (currentAvailabilityState is UsernameAvailabilityUiState.Unavailable) {
            uiState = RegisterUiState.Error(currentAvailabilityState.message)
            return
        }

        val emailError = AccountValidators.validateEmail(normalizedEmail)
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
                    username = normalizedUsername,
                    email = normalizedEmail,
                    password = password,
                    age = age
                )

                val response = userRepository.registerUser(request)

                if (response.isSuccessful && response.body() != null) {
                    uiState = RegisterUiState.Success(
                        "Cuenta creada correctamente."
                    )
                } else {
                    val errorMessage = extractErrorMessage(
                        errorBody = response.errorBody()?.string()
                    )

                    uiState = RegisterUiState.Error(
                        errorMessage ?: "No se pudo crear la cuenta."
                    )
                }
            } catch (e: Exception) {
                uiState = RegisterUiState.Error("Error de conexión con la API.")
            }
        }
    }

    fun resetState() {
        uiState = RegisterUiState.Idle
    }

    private fun extractErrorMessage(errorBody: String?): String? {
        return try {
            Gson().fromJson(
                errorBody,
                MessageResponse::class.java
            )?.message
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        private const val USERNAME_CHECK_DELAY = 500L
    }
}

sealed class UsernameAvailabilityUiState {
    data object Idle : UsernameAvailabilityUiState()
    data object Checking : UsernameAvailabilityUiState()

    data class Available(
        val message: String
    ) : UsernameAvailabilityUiState()

    data class Unavailable(
        val message: String
    ) : UsernameAvailabilityUiState()

    data class Error(
        val message: String
    ) : UsernameAvailabilityUiState()
}