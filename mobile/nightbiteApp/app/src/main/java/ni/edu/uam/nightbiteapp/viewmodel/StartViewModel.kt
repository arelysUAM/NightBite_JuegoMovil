package ni.edu.uam.nightbiteapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ni.edu.uam.nightbiteapp.data.local.session.SessionManager
import ni.edu.uam.nightbiteapp.data.repository.UserRepository
import ni.edu.uam.nightbiteapp.data.local.session.UserSession
import ni.edu.uam.nightbiteapp.data.remote.dto.PlayerSummaryResponse
import ni.edu.uam.nightbiteapp.data.remote.dto.UserResponse

private const val POST_API_SPLASH_DELAY = 3000L

class StartViewModel(
    private val sessionManager: SessionManager,
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<StartUiState>(
        StartUiState.Loading()
    )
    val uiState: StateFlow<StartUiState> = _uiState

    private var checkInitialStateJob: Job? = null

    fun checkInitialState(requestId: Long) {
        checkInitialStateJob?.cancel()

        _uiState.value = StartUiState.Loading(
            requestId = requestId
        )

        checkInitialStateJob = viewModelScope.launch {
            val nextState = try {
                val session = sessionManager.userSessionFlow.first()

                if (session.isLoggedIn && session.userId != null) {
                    StartUiState.NavigateToHome(
                        user = session.toUserResponse(),
                        requestId = requestId
                    )
                } else {
                    val healthResponse = userRepository.checkHealth()

                    if (!healthResponse.isSuccessful) {
                        StartUiState.ServerError(
                            message = "El servidor no respondió correctamente.",
                            requestId = requestId
                        )
                    } else {
                        StartUiState.NavigateToLogin(
                            requestId = requestId
                        )
                    }
                }
            } catch (e: Exception) {
                val session = sessionManager.userSessionFlow.first()

                if (session.isLoggedIn && session.userId != null) {
                    StartUiState.NavigateToHome(
                        user = session.toUserResponse(),
                        requestId = requestId
                    )
                } else {
                    StartUiState.ServerError(
                        message = "No se pudo conectar con el servidor.",
                        requestId = requestId
                    )
                }
            }

            delay(POST_API_SPLASH_DELAY)

            _uiState.value = nextState
        }
    }

    fun retry(requestId: Long) {
        checkInitialState(requestId)
    }

    fun continueToLogin(requestId: Long) {
        _uiState.value = StartUiState.NavigateToLogin(
            requestId = requestId
        )
    }

    private suspend fun resolveSessionState(requestId: Long): StartUiState {
        val session = sessionManager.userSessionFlow.first()
        val userId = session.userId

        if (userId == null) {
            return StartUiState.NavigateToLogin(
                requestId = requestId
            )
        }

        val userResponse = userRepository.getUserById(userId)

        return if (userResponse.isSuccessful && userResponse.body() != null) {
            StartUiState.NavigateToHome(
                user = userResponse.body()!!,
                requestId = requestId
            )
        } else {
            sessionManager.clearSession()

            StartUiState.NavigateToLogin(
                requestId = requestId
            )
        }
    }

    private fun UserSession.toUserResponse(): UserResponse {
        return UserResponse(
            id = userId ?: 0L,
            username = username,
            email = email,
            age = age ?: 13,
            player = if (hasPlayer) {
                PlayerSummaryResponse(
                    id = 0L,
                    driverName = username.ifBlank { "Delivery" },
                    gender = playerGender,
                    helmetColor = "Negro",
                    motorcycleType = "Delivery"
                )
            } else {
                null
            }
        )
    }
}