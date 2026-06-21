package ni.edu.uam.nightbiteapp.viewmodel

data class PlayerCreationUiState(
    val driverName: String = "",
    val gender: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isPlayerCreated: Boolean = false
)