package ni.edu.uam.nightbiteapp.viewmodel

import ni.edu.uam.nightbiteapp.data.remote.dto.UserResponse
import ni.edu.uam.nightbiteapp.ui.model.NightLevel

data class HomeUiState(
    val isLoading: Boolean = false,
    val user: UserResponse? = null,
    val levels: List<NightLevel> = emptyList(),
    val errorMessage: String? = null
) {
    val hasPlayer: Boolean
        get() = user?.player != null
}