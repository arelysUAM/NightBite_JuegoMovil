package ni.edu.uam.nightbiteapp.data.remote.dto

data class PlayerRequest(
    val userAccountId: Long,
    val driverName: String,
    val gender: String,
    val helmetColor: String,
    val motorcycleType: String
)