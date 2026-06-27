package ni.edu.uam.nightbiteapp.game

/**
 * Resultado final del tutorial jugable en LibGDX.
 *
 * Este modelo se enviará desde LibGDX hacia Compose para:
 * - abrir GameResultScreen,
 * - guardar el progreso en Room,
 * - desbloquear la siguiente jornada si obtiene 3 estrellas.
 */
data class TutorialGameResult(
    val completedOrders: Int,
    val totalOrders: Int = TOTAL_TUTORIAL_ORDERS,
    val score: Int,
    val elapsedTimeSeconds: Float,
    val totalDeliveryTimeSeconds: Float = 0f,
    val stars: Int = calculateStars(
        completedOrders = completedOrders,
        totalOrders = totalOrders
    )
) {
    val safeCompletedOrders: Int
        get() = completedOrders.coerceIn(0, totalOrders)

    val safeTotalOrders: Int
        get() = totalOrders.coerceAtLeast(1)

    val ordersText: String
        get() = "$safeCompletedOrders/$safeTotalOrders"

    val elapsedTimeText: String
        get() = formatElapsedTime(elapsedTimeSeconds)

    val averageDeliveryTimeText: String
        get() = formatAverageDeliveryTime(
            totalDeliveryTimeSeconds = totalDeliveryTimeSeconds,
            completedOrders = completedOrders
        )

    val isPerfectResult: Boolean
        get() = stars == 3

    /**
     * Nombre compatible con GameResultType.
     *
     * Se deja como String para que el paquete game no dependa directamente
     * de ui.model.GameResultType.
     */
    val resultTypeName: String
        get() = when (stars) {
            3 -> "TUTORIAL_ALL_DELIVERIES"
            2 -> "TUTORIAL_EIGHTY_PERCENT"
            1 -> "TUTORIAL_HALF_DELIVERIES"
            else -> "TUTORIAL_FIRED"
        }

    companion object {
        const val TOTAL_TUTORIAL_ORDERS = 8

        fun calculateStars(
            completedOrders: Int,
            totalOrders: Int
        ): Int {
            val safeTotalOrders = totalOrders.coerceAtLeast(1)
            val safeCompletedOrders = completedOrders.coerceIn(
                minimumValue = 0,
                maximumValue = safeTotalOrders
            )

            val eightyPercentOrders = ((safeTotalOrders * 80) + 99) / 100
            val halfOrders = (safeTotalOrders + 1) / 2

            return when {
                safeCompletedOrders >= safeTotalOrders -> 3
                safeCompletedOrders >= eightyPercentOrders -> 2
                safeCompletedOrders >= halfOrders -> 1
                else -> 0
            }
        }

        fun formatAverageDeliveryTime(
            totalDeliveryTimeSeconds: Float,
            completedOrders: Int
        ): String {
            if (completedOrders <= 0) return "--"

            val average = totalDeliveryTimeSeconds / completedOrders

            return "${String.format("%.1f", average)}s"
        }

        fun formatElapsedTime(
            elapsedTimeSeconds: Float
        ): String {
            val safeSeconds = elapsedTimeSeconds
                .toInt()
                .coerceAtLeast(0)

            val minutes = safeSeconds / 60
            val seconds = safeSeconds % 60

            return "$minutes:${seconds.toString().padStart(2, '0')}"
        }
    }
}