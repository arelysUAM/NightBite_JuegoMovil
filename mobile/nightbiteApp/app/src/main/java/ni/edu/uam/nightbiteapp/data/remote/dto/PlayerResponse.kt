package ni.edu.uam.nightbiteapp.data.remote.dto

/**
 * DTO utilizado para recibir la información de la ficha/personaje del repartidor desde la API.
 */
data class PlayerResponse(
    val id: Long,
    val userAccountId: Long,
    val nickname: String,
    val driverName: String,
    val gender: String,
    val helmetColor: String,
    val motorcycleType: String,
    val createdAt: String? = null,
    val updatedAt: String? = null
)