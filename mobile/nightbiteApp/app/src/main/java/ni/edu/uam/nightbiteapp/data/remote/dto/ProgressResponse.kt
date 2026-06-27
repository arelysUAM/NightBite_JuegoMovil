package ni.edu.uam.nightbiteapp.data.remote.dto

data class ProgressResponse(
    val userAccountId: Long,
    val maxUnlockedLevel: Int,
    val updatedAt: String?,
    val levelResults: List<LevelResultProgressResponse>,
    val badges: List<UserBadgeResponse>
)