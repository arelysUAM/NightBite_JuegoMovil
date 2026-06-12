package ni.edu.uam.nightbiteapp.data.local.session

/**
 * Representa los datos básicos de la sesión local del usuario.
 *
 * No debe guardar contraseña ni passwordHash.
 */
data class UserSession(
    val isLoggedIn: Boolean = false,
    val userId: Long? = null,
    val username: String = "",
    val email: String = "",
    val age: Int? = null,
    val token: String = ""
)