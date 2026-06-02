package ni.edu.uam.nightbiteapp.data.remote.dto

/**
 * DTO utilizado para recibir la información pública de una cuenta de usuario.
 *
 * No incluye contraseña ni passwordHash por seguridad.
 */
data class UserResponse(
    val id: Long,
    val username: String,
    val email: String,
    val age: Int,
    val createdAt: String? = null
)