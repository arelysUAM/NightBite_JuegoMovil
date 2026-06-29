package ni.edu.uam.nightbiteapp.ui.model

import java.util.Locale

/**
 * Resultado final de una jornada jugable.
 *
 * Se usa para:
 * - abrir GameResultScreen,
 * - mostrar métricas reales de la partida,
 * - guardar progreso,
 * - desbloquear la siguiente jornada si obtiene 3 estrellas.
 */
data class GameRuntimeResult(
    val completedOrders: Int,
    val totalOrders: Int,
    val score: Int,
    val elapsedTimeSeconds: Float,
    val totalDeliveryTimeSeconds: Float,
    val stars: Int
) {
    val elapsedTimeText: String
        get() = formatTime(elapsedTimeSeconds)

    val averageDeliveryTimeText: String
        get() {
            if (completedOrders <= 0) return "--"

            val average = totalDeliveryTimeSeconds / completedOrders

            return "${String.format(Locale.US, "%.1f", average)}s"
        }

    companion object {
        fun formatTime(
            seconds: Float
        ): String {
            val totalSeconds = seconds
                .toInt()
                .coerceAtLeast(0)

            val minutes = totalSeconds / 60
            val remainingSeconds = totalSeconds % 60

            return "$minutes:${remainingSeconds.toString().padStart(2, '0')}"
        }
    }
}