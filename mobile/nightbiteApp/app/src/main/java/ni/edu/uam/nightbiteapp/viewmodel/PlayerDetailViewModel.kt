package ni.edu.uam.nightbiteapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ni.edu.uam.nightbiteapp.data.repository.UserRepository

class PlayerDetailViewModel(
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    var uiState by mutableStateOf<PlayerDetailUiState>(PlayerDetailUiState.Idle)
        private set

    fun loadPlayerDetail(userId: Long) {
        uiState = PlayerDetailUiState.Loading

        viewModelScope.launch {
            try {
                val response = userRepository.getUserById(userId)

                uiState = if (response.isSuccessful && response.body() != null) {
                    PlayerDetailUiState.Success(response.body()!!)
                } else {
                    PlayerDetailUiState.Error("No se pudo cargar la ficha del repartidor.")
                }
            } catch (e: Exception) {
                uiState = PlayerDetailUiState.Error("Error de conexión con la API.")
            }
        }
    }

    fun resetState() {
        uiState = PlayerDetailUiState.Idle
    }
}