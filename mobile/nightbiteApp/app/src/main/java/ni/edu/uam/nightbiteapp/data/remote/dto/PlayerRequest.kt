package ni.edu.uam.nightbiteapp.data.remote.dto

/**
 * DTO utilizado para enviar los datos de la ficha/personaje del repartidor hacia la API.
 *
 * Player ya no representa la cuenta real de inicio de sesión.
 * La cuenta real pertenece a UserAccount.
 */
data class PlayerRequest(
    val userAccountId: Long,
    val nickname: String,
    val driverName: String,
    val gender: String,
    val helmetColor: String,
    val motorcycleType: String
)