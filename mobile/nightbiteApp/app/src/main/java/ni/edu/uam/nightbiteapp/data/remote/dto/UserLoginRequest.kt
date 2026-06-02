package ni.edu.uam.nightbiteapp.data.remote.dto

/**
 * DTO utilizado para iniciar sesión con usuario/correo y contraseña.
 */
data class UserLoginRequest(
    val usernameOrEmail: String,
    val password: String
)