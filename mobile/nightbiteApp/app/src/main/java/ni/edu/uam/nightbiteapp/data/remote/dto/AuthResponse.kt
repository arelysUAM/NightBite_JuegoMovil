package ni.edu.uam.nightbiteapp.data.remote.dto

/**
 * Respuesta recibida al iniciar sesión correctamente.
 *
 * El backend devuelve un token JWT y los datos públicos del usuario.
 */
data class AuthResponse(
    val token: String,
    val user: UserResponse
)