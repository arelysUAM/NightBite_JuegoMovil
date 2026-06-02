package ni.edu.uam.nightbiteapp.viewmodel

/**
 * Representa los posibles estados de la pantalla de registro.
 *
 * Permite comunicar a la interfaz si el registro está en espera,
 * cargando, fue exitoso o presentó un error.
 */
sealed class RegisterUiState {
    object Idle : RegisterUiState()
    object Loading : RegisterUiState()

    data class Success(
        val message: String
    ) : RegisterUiState()

    data class Error(
        val message: String
    ) : RegisterUiState()
}