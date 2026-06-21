package ni.edu.uam.nightbiteapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ni.edu.uam.nightbiteapp.data.local.mock.NightLevelsData
import ni.edu.uam.nightbiteapp.data.local.mock.NightProgressData
import ni.edu.uam.nightbiteapp.data.repository.UserRepository
import ni.edu.uam.nightbiteapp.ui.model.NightLevel

class HomeViewModel(
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        HomeUiState(
            levels = NightLevelsData.levels
        )
    )

    val uiState: StateFlow<HomeUiState> = _uiState

    fun loadHomeData(userId: Long?) {
        val maxUnlockedLevelId = NightProgressData.getMaxUnlockedLevel(userId)
        val rawLevelsByProgress = NightLevelsData.getLevelsByProgress(maxUnlockedLevelId)
        val levelStars = getLevelStars(userId)
        val levelsByProgress = applyStarsToLevels(
            levels = rawLevelsByProgress,
            levelStars = levelStars
        )
        val tutorialStars = levelStars[0] ?: 0

        if (userId == null) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    user = null,
                    levels = levelsByProgress,
                    tutorialStars = tutorialStars,
                    levelStars = levelStars,
                    errorMessage = "No hay una sesión activa.",
                    userLoadFailed = true
                )
            }
            return
        }

        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(
                        isLoading = true,
                        errorMessage = null,
                        levels = levelsByProgress,
                        tutorialStars = tutorialStars,
                        levelStars = levelStars,
                        userLoadFailed = false
                    )
                }

                val response = userRepository.getUserById(userId)

                if (response.isSuccessful && response.body() != null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            user = response.body(),
                            levels = levelsByProgress,
                            tutorialStars = tutorialStars,
                            levelStars = levelStars,
                            errorMessage = null,
                            userLoadFailed = false
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            user = it.user,
                            levels = levelsByProgress,
                            tutorialStars = tutorialStars,
                            levelStars = levelStars,
                            errorMessage = when (response.code()) {
                                401 -> "No se pudo validar la sesión. Inicia sesión nuevamente."
                                403 -> "No tienes permiso para cargar esta cuenta."
                                else -> "No se pudo cargar la información del usuario."
                            },
                            userLoadFailed = true
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        user = it.user,
                        levels = levelsByProgress,
                        tutorialStars = tutorialStars,
                        levelStars = levelStars,
                        errorMessage = "Error de conexión con la API.",
                        userLoadFailed = true
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update {
            it.copy(
                errorMessage = null,
                userLoadFailed = false
            )
        }
    }

    private fun getLevelStars(userId: Long?): Map<Int, Int> {
        val tutorialStars = NightProgressData.getTutorialStars(userId)

        return mapOf(
            0 to tutorialStars,
            1 to 0,
            2 to 0,
            3 to 0,
            4 to 0
        )
    }

    private fun applyStarsToLevels(
        levels: List<NightLevel>,
        levelStars: Map<Int, Int>
    ): List<NightLevel> {
        return levels.map { level ->
            level.copy(
                stars = if (level.isUnlocked) {
                    levelStars[level.id]?.coerceIn(0, 3) ?: 0
                } else {
                    0
                }
            )
        }
    }
}