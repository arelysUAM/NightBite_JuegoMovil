package ni.edu.uam.nightbiteapp.viewmodel

import ni.edu.uam.nightbiteapp.data.remote.dto.UserResponse
import ni.edu.uam.nightbiteapp.ui.model.NightLevel

data class HomeUiState(
    val isLoading: Boolean = false,
    val user: UserResponse? = null,
    val levels: List<NightLevel> = emptyList(),
    val tutorialStars: Int = 0,
    val levelStars: Map<Int, Int> = emptyMap(),
    val errorMessage: String? = null,
    val userLoadFailed: Boolean = false
) {
    val hasPlayer: Boolean
        get() = user?.player != null

    val canValidatePlayer: Boolean
        get() = user != null && !userLoadFailed
}