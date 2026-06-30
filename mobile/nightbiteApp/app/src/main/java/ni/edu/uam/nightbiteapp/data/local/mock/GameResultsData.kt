package ni.edu.uam.nightbiteapp.data.local.mock

import ni.edu.uam.nightbiteapp.ui.model.GameResultContent
import ni.edu.uam.nightbiteapp.ui.model.GameResultPalette
import ni.edu.uam.nightbiteapp.ui.model.GameResultType

/**
 * Fuente temporal de resultados del juego.
 *
 * Mientras no exista gameplay real, este archivo estandariza los resultados
 * que se muestran desde GamePlaceholderScreen.
 *
 * Equivalencia de niveles:
 * - levelId 0 -> Noche 1 / Tutorial
 * - levelId 1 -> Noche 2
 * - levelId 2 -> Noche 3
 * - levelId 3 -> Noche 4
 * - levelId 4 -> Noche 5 / Final
 */
object GameResultsData {

    fun getResultContent(
        levelId: Int,
        resultType: GameResultType,
        stars: Int = resultType.defaultStars
    ): GameResultContent {
        val safeLevelId = levelId.coerceIn(MIN_LEVEL_ID, MAX_LEVEL_ID)
        val safeStars = stars.coerceIn(MIN_STARS, MAX_STARS)

        return when (resultType) {
            /*
             * Noche 1 - Tutorial
             */
            GameResultType.TUTORIAL_ALL_DELIVERIES -> tutorialAllDeliveries(
                stars = safeStars
            )

            GameResultType.TUTORIAL_EIGHTY_PERCENT -> tutorialEightyPercent(
                stars = safeStars
            )

            GameResultType.TUTORIAL_HALF_DELIVERIES -> tutorialHalfDeliveries(
                stars = safeStars
            )

            GameResultType.TUTORIAL_FIRED -> tutorialFired()

            GameResultType.TUTORIAL_TIME_EXPIRED -> tutorialTimeExpired(
                stars = safeStars
            )

            /*
             * Noches 2, 3 y 4
             */
            GameResultType.LEVEL_WIN -> levelWin(
                levelId = safeLevelId,
                stars = safeStars
            )

            GameResultType.LEVEL_INCOMPLETE_AT_LEAST_50 -> levelIncompleteAtLeast50(
                levelId = safeLevelId,
                stars = safeStars
            )

            GameResultType.LEVEL_INCOMPLETE_UNDER_50 -> levelIncompleteUnder50(
                levelId = safeLevelId,
                stars = safeStars
            )

            GameResultType.LEVEL_OUT_OF_LIVES -> levelOutOfLives(
                levelId = safeLevelId
            )

            GameResultType.LEVEL_TIME_EXPIRED -> levelTimeExpired(
                levelId = safeLevelId,
                stars = safeStars
            )

            /*
             * Noche 5 - Final
             */
            GameResultType.FINAL_WIN -> finalWin(
                stars = safeStars
            )

            GameResultType.FINAL_INCOMPLETE_AT_LEAST_50 -> finalIncompleteAtLeast50(
                stars = safeStars
            )

            GameResultType.FINAL_INCOMPLETE_UNDER_50 -> finalIncompleteUnder50(
                stars = safeStars
            )

            GameResultType.FINAL_OUT_OF_LIVES -> finalOutOfLives()

            GameResultType.FINAL_TIME_EXPIRED -> finalTimeExpired(
                stars = safeStars
            )
        }
    }

    private fun tutorialAllDeliveries(
        stars: Int
    ): GameResultContent {
        val totalOrders = TUTORIAL_TOTAL_ORDERS

        return GameResultContent(
            title = "Tutorial Completado",
            subtitle = "Completaste todos los pedidos a tiempo",
            message = "Dominaste tu primera jornada como repartidor nocturno.",
            stars = stars.coerceToDefault(
                defaultStars = GameResultType.TUTORIAL_ALL_DELIVERIES.defaultStars
            ),
            timeText = "2:45",
            completedOrders = totalOrders,
            totalOrders = totalOrders,
            palette = GameResultPalette.PURPLE,
            details = listOf(
                "Pedidos entregados: $totalOrders/$totalOrders",
                "Calificación obtenida: 3 estrellas",
                "La siguiente noche ha sido desbloqueada"
            ),
            rewardMessage = "Noche 2 desbloqueada",
            illustrationDescription = "Tres estrellas sobre la motocicleta del repartidor",
            showContinueButton = true,
            showRetryButton = true,
            showHomeButton = true
        )
    }

    private fun tutorialEightyPercent(
        stars: Int
    ): GameResultContent {
        val totalOrders = TUTORIAL_TOTAL_ORDERS
        val completedOrders = eightyPercentOrders(totalOrders)

        return GameResultContent(
            title = "Tutorial Superado",
            subtitle = "Con un poco más de práctica serás imparable",
            message = "Entregaste al menos el 80 % de los pedidos a tiempo.",
            stars = stars.coerceToDefault(
                defaultStars = GameResultType.TUTORIAL_EIGHTY_PERCENT.defaultStars
            ),
            timeText = "3:05",
            completedOrders = completedOrders,
            totalOrders = totalOrders,
            palette = GameResultPalette.PURPLE,
            details = listOf(
                "Pedidos entregados: $completedOrders/$totalOrders",
                "Calificación obtenida: 2 estrellas",
                "Debes completar todos los pedidos para desbloquear la siguiente noche"
            ),
            illustrationDescription = "Dos estrellas sobre una bolsa de pedidos",
            showContinueButton = false,
            showRetryButton = true,
            showHomeButton = true
        )
    }

    private fun tutorialHalfDeliveries(
        stars: Int
    ): GameResultContent {
        val totalOrders = TUTORIAL_TOTAL_ORDERS
        val completedOrders = halfOrMoreOrders(totalOrders)

        return GameResultContent(
            title = "Tutorial Superado",
            subtitle = "Eres bueno, pero no lo suficiente",
            message = "Entregaste al menos la mitad de los pedidos, pero debes mejorar tu ritmo.",
            stars = stars.coerceToDefault(
                defaultStars = GameResultType.TUTORIAL_HALF_DELIVERIES.defaultStars
            ),
            timeText = "3:20",
            completedOrders = completedOrders,
            totalOrders = totalOrders,
            palette = GameResultPalette.PURPLE,
            details = listOf(
                "Pedidos entregados: $completedOrders/$totalOrders",
                "Calificación obtenida: 1 estrella",
                "Debes completar todos los pedidos para desbloquear la siguiente noche"
            ),
            illustrationDescription = "Una estrella sobre una caja de entrega",
            showContinueButton = false,
            showRetryButton = true,
            showHomeButton = true
        )
    }

    private fun tutorialFired(): GameResultContent {
        val totalOrders = TUTORIAL_TOTAL_ORDERS
        val completedOrders = underHalfOrders(totalOrders)

        return GameResultContent(
            title = "Despedido",
            subtitle = "No eres lo suficientemente rápido para este empleo",
            message = "No completaste suficientes pedidos durante el tutorial.",
            stars = GameResultType.TUTORIAL_FIRED.defaultStars,
            timeText = "3:28",
            completedOrders = completedOrders,
            totalOrders = totalOrders,
            palette = GameResultPalette.RED,
            details = listOf(
                "Pedidos entregados: $completedOrders/$totalOrders",
                "Calificación obtenida: 0 estrellas",
                "El restaurante ha terminado tu contrato"
            ),
            illustrationDescription = "Contrato de repartidor marcado como despedido",
            showContinueButton = false,
            showRetryButton = true,
            showHomeButton = true
        )
    }

    private fun tutorialTimeExpired(
        stars: Int
    ): GameResultContent {
        val totalOrders = TUTORIAL_TOTAL_ORDERS

        return GameResultContent(
            title = "Tiempo agotado",
            subtitle = "La noche avanzó demasiado rápido",
            message = "Debiste moverte más rápido si querías completar tu primera jornada.",
            stars = stars.coerceIn(MIN_STARS, MAX_STARS),
            timeText = "3:00",
            completedOrders = 0,
            totalOrders = totalOrders,
            palette = GameResultPalette.RED,
            details = listOf(
                "Tiempo máximo alcanzado: 3:00",
                "Pedidos entregados: 0/$totalOrders",
                "Calificación obtenida: ${stars.coerceIn(MIN_STARS, MAX_STARS)} estrellas",
                "Debes mejorar tu ritmo para sobrevivir a la noche"
            ),
            illustrationDescription = "Reloj nocturno marcando el final del turno",
            showContinueButton = false,
            showRetryButton = true,
            showHomeButton = true
        )
    }

    private fun levelWin(
        levelId: Int,
        stars: Int
    ): GameResultContent {
        val totalOrders = totalOrdersForLevel(levelId)

        return GameResultContent(
            title = "Jornada Completa",
            subtitle = "Todos los pedidos fueron entregados a tiempo",
            message = "Completaste la jornada, sobreviviste y regresaste al restaurante.",
            stars = stars.coerceToDefault(
                defaultStars = GameResultType.LEVEL_WIN.defaultStars
            ),
            timeText = "2:58",
            completedOrders = totalOrders,
            totalOrders = totalOrders,
            palette = GameResultPalette.PURPLE,
            details = listOf(
                "Noche completada: ${displayNightNumber(levelId)}",
                "Pedidos entregados: $totalOrders/$totalOrders",
                "Estado del repartidor: a salvo",
                "Calificación obtenida: 3 estrellas"
            ),
            rewardMessage = "Siguiente noche desbloqueada",
            illustrationDescription = "Insignia nocturna junto al casco del repartidor",
            showContinueButton = true,
            showRetryButton = true,
            showHomeButton = true
        )
    }

    private fun levelIncompleteAtLeast50(
        levelId: Int,
        stars: Int
    ): GameResultContent {
        val totalOrders = totalOrdersForLevel(levelId)
        val completedOrders = halfOrMoreOrders(totalOrders)

        return GameResultContent(
            title = "Jornada Superada",
            subtitle = "Sobreviviste, pero quedaron pedidos pendientes",
            message = "Entregaste al menos la mitad de los pedidos a tiempo.",
            stars = stars.coerceToDefault(
                defaultStars = GameResultType.LEVEL_INCOMPLETE_AT_LEAST_50.defaultStars
            ),
            timeText = "3:00",
            completedOrders = completedOrders,
            totalOrders = totalOrders,
            palette = GameResultPalette.PURPLE,
            details = listOf(
                "Noche jugada: ${displayNightNumber(levelId)}",
                "Pedidos entregados: $completedOrders/$totalOrders",
                "Calificación obtenida: 2 estrellas",
                "La jornada no fue completada al 100 %"
            ),
            illustrationDescription = "Pedidos pendientes frente al restaurante",
            showContinueButton = false,
            showRetryButton = true,
            showHomeButton = true
        )
    }

    private fun levelIncompleteUnder50(
        levelId: Int,
        stars: Int
    ): GameResultContent {
        val totalOrders = totalOrdersForLevel(levelId)
        val completedOrders = underHalfOrders(totalOrders)

        return GameResultContent(
            title = "Jornada Superada",
            subtitle = "Entregaste pocos pedidos antes de terminar la noche",
            message = "Sobreviviste, pero entregaste menos de la mitad de los pedidos a tiempo.",
            stars = stars.coerceToDefault(
                defaultStars = GameResultType.LEVEL_INCOMPLETE_UNDER_50.defaultStars
            ),
            timeText = "3:00",
            completedOrders = completedOrders,
            totalOrders = totalOrders,
            palette = GameResultPalette.PURPLE,
            details = listOf(
                "Noche jugada: ${displayNightNumber(levelId)}",
                "Pedidos entregados: $completedOrders/$totalOrders",
                "Calificación obtenida: 1 estrella",
                "Necesitas mejorar para completar la jornada"
            ),
            illustrationDescription = "Bolsa de pedidos olvidada en la calle",
            showContinueButton = false,
            showRetryButton = true,
            showHomeButton = true
        )
    }

    private fun levelOutOfLives(
        levelId: Int
    ): GameResultContent {
        val totalOrders = totalOrdersForLevel(levelId)
        val completedOrders = 0

        return GameResultContent(
            title = "Fin de la jornada",
            subtitle = "Te quedaste sin vidas",
            message = "La ciudad quedó en silencio después de tu último intento.",
            stars = GameResultType.LEVEL_OUT_OF_LIVES.defaultStars,
            timeText = "3:28",
            completedOrders = completedOrders,
            totalOrders = totalOrders,
            palette = GameResultPalette.RED,
            details = listOf(
                "Noche jugada: ${displayNightNumber(levelId)}",
                "Vidas restantes: 0",
                "Pedidos entregados: $completedOrders/$totalOrders",
                "Calificación obtenida: 0 estrellas"
            ),
            illustrationDescription = "Casco del repartidor abandonado bajo un poste de luz",
            showContinueButton = false,
            showRetryButton = true,
            showHomeButton = true
        )
    }

    private fun levelTimeExpired(
        levelId: Int,
        stars: Int
    ): GameResultContent {
        val totalOrders = totalOrdersForLevel(levelId)
        val safeStars = stars.coerceIn(MIN_STARS, MAX_STARS)

        return GameResultContent(
            title = "Tiempo agotado",
            subtitle = "La noche no espera a nadie",
            message = "La jornada terminó antes de que pudieras completar la ruta.",
            stars = safeStars,
            timeText = "3:00",
            completedOrders = 0,
            totalOrders = totalOrders,
            palette = GameResultPalette.RED,
            details = listOf(
                "Noche jugada: ${displayNightNumber(levelId)}",
                "Tiempo máximo alcanzado: 3:00",
                "Calificación obtenida: $safeStars estrellas",
                "Necesitas completar más entregas antes de que termine la noche"
            ),
            illustrationDescription = "Reloj nocturno sobre una calle vacía",
            showContinueButton = false,
            showRetryButton = true,
            showHomeButton = true
        )
    }

    private fun finalWin(
        stars: Int
    ): GameResultContent {
        val totalOrders = FINAL_TOTAL_ORDERS

        return GameResultContent(
            title = "Jornada Completa",
            subtitle = "Sobreviviste a la última noche",
            message = "Completaste todos los pedidos a tiempo y lograste salir del bucle.",
            stars = stars.coerceToDefault(
                defaultStars = GameResultType.FINAL_WIN.defaultStars
            ),
            timeText = "2:59",
            completedOrders = totalOrders,
            totalOrders = totalOrders,
            palette = GameResultPalette.PURPLE,
            details = listOf(
                "Noche completada: ${displayNightNumber(FINAL_LEVEL_ID)}",
                "Pedidos entregados: $totalOrders/$totalOrders",
                "Estado del repartidor: libre",
                "Calificación obtenida: 3 estrellas"
            ),
            rewardMessage = "Recibiste tu paga final",
            illustrationDescription = "El repartidor saliendo del restaurante con su pago",
            isFinalResult = true,
            showContinueButton = true,
            showRetryButton = true,
            showHomeButton = true
        )
    }

    private fun finalIncompleteAtLeast50(
        stars: Int
    ): GameResultContent {
        val totalOrders = FINAL_TOTAL_ORDERS
        val completedOrders = halfOrMoreOrders(totalOrders)

        return GameResultContent(
            title = "Jornada Superada",
            subtitle = "Sobreviviste, pero la última ruta quedó incompleta",
            message = "Entregaste al menos la mitad de los pedidos finales a tiempo.",
            stars = stars.coerceToDefault(
                defaultStars = GameResultType.FINAL_INCOMPLETE_AT_LEAST_50.defaultStars
            ),
            timeText = "3:00",
            completedOrders = completedOrders,
            totalOrders = totalOrders,
            palette = GameResultPalette.RED,
            details = listOf(
                "Noche jugada: ${displayNightNumber(FINAL_LEVEL_ID)}",
                "Pedidos entregados: $completedOrders/$totalOrders",
                "Calificación obtenida: 2 estrellas",
                "El cierre de la jornada quedó incompleto"
            ),
            illustrationDescription = "Pedidos finales pendientes frente al restaurante",
            isFinalResult = true,
            showContinueButton = false,
            showRetryButton = true,
            showHomeButton = true
        )
    }

    private fun finalIncompleteUnder50(
        stars: Int
    ): GameResultContent {
        val totalOrders = FINAL_TOTAL_ORDERS
        val completedOrders = underHalfOrders(totalOrders)

        return GameResultContent(
            title = "Jornada Superada",
            subtitle = "La última noche fue demasiado pesada",
            message = "Sobreviviste, pero entregaste menos de la mitad de los pedidos finales.",
            stars = stars.coerceToDefault(
                defaultStars = GameResultType.FINAL_INCOMPLETE_UNDER_50.defaultStars
            ),
            timeText = "3:00",
            completedOrders = completedOrders,
            totalOrders = totalOrders,
            palette = GameResultPalette.RED,
            details = listOf(
                "Noche jugada: ${displayNightNumber(FINAL_LEVEL_ID)}",
                "Pedidos entregados: $completedOrders/$totalOrders",
                "Calificación obtenida: 1 estrella",
                "El repartidor no logró cerrar la jornada final"
            ),
            illustrationDescription = "El repartidor detenido antes de completar la última ruta",
            isFinalResult = true,
            showContinueButton = false,
            showRetryButton = true,
            showHomeButton = true
        )
    }

    private fun finalOutOfLives(): GameResultContent {
        val totalOrders = FINAL_TOTAL_ORDERS
        val completedOrders = 0

        return GameResultContent(
            title = "Fin de la jornada",
            subtitle = "Te quedaste sin vidas",
            message = "No lograste sobrevivir a la última noche.",
            stars = GameResultType.FINAL_OUT_OF_LIVES.defaultStars,
            timeText = "3:28",
            completedOrders = completedOrders,
            totalOrders = totalOrders,
            palette = GameResultPalette.RED,
            details = listOf(
                "Noche jugada: ${displayNightNumber(FINAL_LEVEL_ID)}",
                "Vidas restantes: 0",
                "Pedidos entregados: $completedOrders/$totalOrders",
                "Calificación obtenida: 0 estrellas"
            ),
            illustrationDescription = "El repartidor perdido dentro de la dimensión alterna",
            isFinalResult = true,
            showContinueButton = false,
            showRetryButton = true,
            showHomeButton = true
        )
    }

    private fun finalTimeExpired(
        stars: Int
    ): GameResultContent {
        val totalOrders = FINAL_TOTAL_ORDERS
        val safeStars = stars.coerceIn(MIN_STARS, MAX_STARS)

        return GameResultContent(
            title = "Tiempo agotado",
            subtitle = "La última noche llegó a su límite",
            message = "Estuviste cerca de volver a casa, pero la noche terminó antes que tu ruta.",
            stars = safeStars,
            timeText = "3:00",
            completedOrders = 0,
            totalOrders = totalOrders,
            palette = GameResultPalette.RED,
            details = listOf(
                "Noche jugada: ${displayNightNumber(FINAL_LEVEL_ID)}",
                "Tiempo máximo alcanzado: 3:00",
                "Calificación obtenida: $safeStars estrellas",
                "El repartidor no logró cerrar la jornada final"
            ),
            illustrationDescription = "La luna cubierta marcando el final del turno",
            isFinalResult = true,
            showContinueButton = false,
            showRetryButton = true,
            showHomeButton = true
        )
    }

    private fun totalOrdersForLevel(
        levelId: Int
    ): Int {
        return when (levelId.coerceIn(MIN_LEVEL_ID, MAX_LEVEL_ID)) {
            0 -> TUTORIAL_TOTAL_ORDERS
            1 -> 8
            2 -> 10
            3 -> 12
            else -> FINAL_TOTAL_ORDERS
        }
    }

    private fun displayNightNumber(
        levelId: Int
    ): Int {
        return levelId.coerceIn(MIN_LEVEL_ID, MAX_LEVEL_ID) + 1
    }

    private fun eightyPercentOrders(
        totalOrders: Int
    ): Int {
        return ((totalOrders * 80) + 99) / 100
    }

    private fun halfOrMoreOrders(
        totalOrders: Int
    ): Int {
        return (totalOrders + 1) / 2
    }

    private fun underHalfOrders(
        totalOrders: Int
    ): Int {
        return (halfOrMoreOrders(totalOrders) - 1).coerceAtLeast(0)
    }

    private fun Int.coerceToDefault(
        defaultStars: Int
    ): Int {
        val safeStars = this.coerceIn(MIN_STARS, MAX_STARS)

        return if (safeStars == defaultStars) {
            safeStars
        } else {
            defaultStars
        }
    }

    private const val MIN_LEVEL_ID = 0
    private const val MAX_LEVEL_ID = 4
    private const val FINAL_LEVEL_ID = 4

    private const val MIN_STARS = 0
    private const val MAX_STARS = 3

    private const val TUTORIAL_TOTAL_ORDERS = 8
    private const val FINAL_TOTAL_ORDERS = 15
}