package ni.edu.uam.nightbiteapp.data.remote.dto

/**
 * DTO resumido para representar el Player asignado dentro de UserResponse.
 */
data class PlayerSummaryResponse(
    val id: Long,
    val nickname: String,
    val driverName: String,
    val gender: String,
    val helmetColor: String,
    val motorcycleType: String
)