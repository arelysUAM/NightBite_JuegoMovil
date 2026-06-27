package ni.edu.uam.nightbiteapp.data.remote.dto

data class UserBadgeResponse(
    val levelId: Int,
    val badgeCode: String,
    val unlockedAt: String?
)