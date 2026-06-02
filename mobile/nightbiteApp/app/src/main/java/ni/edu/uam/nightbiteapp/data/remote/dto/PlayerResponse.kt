package ni.edu.uam.nightbiteapp.data.remote.dto

/**
 * DTO utilizado para recibir la información de un jugador desde la API.
 *
 * Permite que la aplicación obtenga los datos del jugador sin depender
 * directamente de la estructura interna de la base de datos.
 */
data class PlayerResponse(
    val id: Long,
    val username: String,
    val email: String,
    val createdAt: String? = null
)