package ni.edu.uam.nightbiteapp.game

/**
 * Estado de un pedido dentro del tutorial.
 */
enum class TutorialOrderStatus {
    WAITING_AT_RESTAURANT,
    IN_PROGRESS,
    DELIVERED,
    EXPIRED
}

/**
 * Representa un pedido del tutorial.
 *
 * El cronómetro empieza cuando el jugador recoge el pedido
 * en el restaurante y termina cuando llega al punto de entrega.
 */
data class TutorialOrder(
    val number: Int,
    val destinationNodeId: String,
    val timeLimitSeconds: Float = DEFAULT_TIME_LIMIT_SECONDS,
    val startedAtSeconds: Float? = null,
    val deliveredAtSeconds: Float? = null,
    val status: TutorialOrderStatus = TutorialOrderStatus.WAITING_AT_RESTAURANT
) {
    val isWaitingAtRestaurant: Boolean
        get() = status == TutorialOrderStatus.WAITING_AT_RESTAURANT

    val isInProgress: Boolean
        get() = status == TutorialOrderStatus.IN_PROGRESS

    val isDelivered: Boolean
        get() = status == TutorialOrderStatus.DELIVERED

    val isExpired: Boolean
        get() = status == TutorialOrderStatus.EXPIRED

    fun start(currentGameTimeSeconds: Float): TutorialOrder {
        return copy(
            startedAtSeconds = currentGameTimeSeconds,
            status = TutorialOrderStatus.IN_PROGRESS
        )
    }

    fun deliver(currentGameTimeSeconds: Float): TutorialOrder {
        return copy(
            deliveredAtSeconds = currentGameTimeSeconds,
            status = TutorialOrderStatus.DELIVERED
        )
    }

    fun expire(): TutorialOrder {
        return copy(
            status = TutorialOrderStatus.EXPIRED
        )
    }

    fun remainingTimeSeconds(currentGameTimeSeconds: Float): Float {
        val startTime = startedAtSeconds ?: return timeLimitSeconds

        if (status != TutorialOrderStatus.IN_PROGRESS) {
            return 0f
        }

        val elapsedTime = currentGameTimeSeconds - startTime
        return (timeLimitSeconds - elapsedTime).coerceAtLeast(0f)
    }

    fun hasTimeExpired(currentGameTimeSeconds: Float): Boolean {
        return status == TutorialOrderStatus.IN_PROGRESS &&
                remainingTimeSeconds(currentGameTimeSeconds) <= 0f
    }

    companion object {
        const val DEFAULT_TIME_LIMIT_SECONDS = 6f
    }
}