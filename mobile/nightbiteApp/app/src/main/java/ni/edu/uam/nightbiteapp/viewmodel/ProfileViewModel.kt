package ni.edu.uam.nightbiteapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ni.edu.uam.nightbiteapp.data.repository.UserRepository

class ProfileViewModel(
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    var uiState by mutableStateOf<ProfileUiState>(ProfileUiState.Idle)
        private set

    fun loadUserProfile(userId: Long) {
        uiState = ProfileUiState.Loading

        viewModelScope.launch {
            try {
                val response = userRepository.getUserById(userId)

                uiState = if (response.isSuccessful && response.body() != null) {
                    ProfileUiState.Success(response.body()!!)
                } else {
                    ProfileUiState.Error("No se pudo cargar el perfil del usuario.")
                }
            } catch (e: Exception) {
                uiState = ProfileUiState.Error("Error de conexión con la API.")
            }
        }
    }

    fun resetState() {
        uiState = ProfileUiState.Idle
    }
}