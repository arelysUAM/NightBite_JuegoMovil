package ni.edu.uam.nightbiteapp.data.remote.dto

data class PlayerResponse(
    val id: Long,
    val userAccountId: Long,
    val driverName: String,
    val gender: String,
    val helmetColor: String,
    val motorcycleType: String,
    val createdAt: String? = null,
    val updatedAt: String? = null
)