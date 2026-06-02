package ni.edu.uam.nightbiteapp.data.remote.dto

/**
 * DTO utilizado para enviar los datos de registro de una cuenta de usuario.
 *
 * Representa la cuenta real del jugador, no el personaje dentro del juego.
 */
data class UserRegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val age: Int
)