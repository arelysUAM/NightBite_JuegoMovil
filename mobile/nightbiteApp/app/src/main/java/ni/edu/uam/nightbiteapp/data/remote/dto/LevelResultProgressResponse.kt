package ni.edu.uam.nightbiteapp.data.remote.dto

data class LevelResultProgressResponse(
    val levelId: Int,
    val bestStars: Int,
    val bestScore: Int,
    val completedOrders: Int,
    val totalOrders: Int,
    val resultType: String,
    val elapsedTimeSeconds: Float,
    val averageDeliveryTimeSeconds: Float,
    val updatedAt: String?
)