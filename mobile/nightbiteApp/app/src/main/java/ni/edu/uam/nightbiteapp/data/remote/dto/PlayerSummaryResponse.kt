package ni.edu.uam.nightbiteapp.data.remote.dto

data class PlayerSummaryResponse(
    val id: Long,
    val driverName: String,
    val gender: String,
    val helmetColor: String,
    val motorcycleType: String
)