package ni.edu.uam.nightbiteapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ni.edu.uam.nightbiteapp.data.local.session.SessionManager
import ni.edu.uam.nightbiteapp.data.repository.UserRepository
import android.os.SystemClock
import kotlinx.coroutines.delay

class StartViewModel(
    private val sessionManager: SessionManager,
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<StartUiState>(StartUiState.Loading)
    val uiState: StateFlow<StartUiState> = _uiState

    fun checkInitialState() {
        viewModelScope.launch {
            _uiState.value = StartUiState.Loading

            val minimumSplashTime = 3000L
            val startTime = SystemClock.elapsedRealtime()

            try {
                val healthResponse = userRepository.checkHealth()

                val nextState = if (!healthResponse.isSuccessful) {
                    StartUiState.ServerError(
                        "El servidor no respondió correctamente."
                    )
                } else {
                    val session = sessionManager.userSessionFlow.first()

                    if (session.userId == null) {
                        StartUiState.NavigateToLogin
                    } else {
                        val userResponse = userRepository.getUserById(session.userId)

                        if (userResponse.isSuccessful && userResponse.body() != null) {
                            StartUiState.NavigateToHome(
                                userResponse.body()!!
                            )
                        } else {
                            sessionManager.clearSession()
                            StartUiState.NavigateToLogin
                        }
                    }
                }

                val elapsedTime = SystemClock.elapsedRealtime() - startTime
                val remainingTime = minimumSplashTime - elapsedTime

                if (remainingTime > 0) {
                    delay(remainingTime)
                }

                _uiState.value = nextState

            } catch (e: Exception) {
                val elapsedTime = SystemClock.elapsedRealtime() - startTime
                val remainingTime = minimumSplashTime - elapsedTime

                if (remainingTime > 0) {
                    delay(remainingTime)
                }

                _uiState.value = StartUiState.ServerError(
                    "No se pudo conectar con el servidor."
                )
            }
        }
    }

    fun retry() {
        checkInitialState()
    }

    fun continueToLogin() {
        _uiState.value = StartUiState.NavigateToLogin
    }
}