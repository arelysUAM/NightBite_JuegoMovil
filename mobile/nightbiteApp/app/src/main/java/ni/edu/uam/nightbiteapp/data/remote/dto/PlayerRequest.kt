package ni.edu.uam.nightbiteapp.data.remote.dto

/**
 * DTO utilizado para enviar los datos de un jugador hacia la API.
 *
 * Se usará principalmente en el registro de nuevos jugadores.
 * Más adelante será enviado mediante Retrofit al endpoint correspondiente
 * del backend desarrollado en Spring Boot.
 */
data class PlayerRequest(
    val username: String,
    val email: String
)