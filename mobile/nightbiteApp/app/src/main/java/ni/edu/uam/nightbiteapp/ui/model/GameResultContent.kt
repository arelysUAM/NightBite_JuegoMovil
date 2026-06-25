package ni.edu.uam.nightbiteapp.ui.model

/**
 * Paleta visual que debe usar la pantalla de resultado.
 *
 * PURPLE:
 * - Fondo fondo_estampado_morado.png
 * - Resultado superado o completado
 *
 * RED:
 * - Fondo partida_perdida.png
 * - Resultado perdido o final negativo
 */
enum class GameResultPalette {
    PURPLE,
    RED
}

/**
 * Modelo visual para una pantalla de resultado.
 *
 * Este contenido no calcula la lógica del juego.
 * Solo guarda lo que la pantalla debe mostrar:
 * título, subtítulo, estrellas, tiempo, pedidos, paleta y botones.
 */
data class GameResultContent(
    val title: String,
    val subtitle: String,
    val message: String,

    /**
     * Estrellas obtenidas en la jornada.
     */
    val stars: Int? = null,

    /**
     * Datos temporales para mostrar en la pantalla de resultado.
     * Más adelante vendrán del gameplay real.
     */
    val timeText: String = "0:00",
    val completedOrders: Int = 0,
    val totalOrders: Int = 8,

    /**
     * Paleta visual de la pantalla.
     */
    val palette: GameResultPalette = GameResultPalette.PURPLE,

    /**
     * Resumen narrativo o técnico del resultado.
     */
    val details: List<String> = emptyList(),

    /**
     * Recompensa o consecuencia del resultado.
     * Ejemplo: "Noche 2 desbloqueada".
     */
    val rewardMessage: String? = null,

    /**
     * Descripción temporal para ilustraciones pendientes.
     */
    val illustrationDescription: String? = null,

    /**
     * Marca resultados correspondientes al nivel final.
     */
    val isFinalResult: Boolean = false,

    /**
     * Control visual de botones.
     *
     * showContinueButton representa el botón "Siguiente".
     */
    val showContinueButton: Boolean = false,
    val showRetryButton: Boolean = true,
    val showHomeButton: Boolean = true
) {
    val safeStars: Int
        get() = (stars ?: 0).coerceIn(0, 3)

    val safeCompletedOrders: Int
        get() = completedOrders.coerceIn(0, totalOrders.coerceAtLeast(0))

    val safeTotalOrders: Int
        get() = totalOrders.coerceAtLeast(0)

    val ordersText: String
        get() = "$safeCompletedOrders/$safeTotalOrders"

    val isPerfectResult: Boolean
        get() = safeStars == 3

    val usesPurplePalette: Boolean
        get() = palette == GameResultPalette.PURPLE

    val usesRedPalette: Boolean
        get() = palette == GameResultPalette.RED
}