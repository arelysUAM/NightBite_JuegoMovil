package ni.edu.uam.nightbiteapp.ui.model

/**
 * Contenido que se mostrará en una pantalla de resultado.
 *
 * Este modelo separa la información narrativa y visual
 * de la lógica de navegación de GameResultScreen.
 */
data class GameResultContent(
    val title: String,
    val subtitle: String,
    val message: String,
    val details: List<String> = emptyList(),

    /**
     * Cantidad de estrellas obtenidas durante el tutorial.
     * Es null para las noches normales y la noche final.
     */
    val stars: Int? = null,

    /**
     * Texto relacionado con una recompensa:
     * medalla, desbloqueo de nivel o paga final.
     */
    val rewardMessage: String? = null,

    /**
     * Describe la ilustración que se agregará después.
     *
     * Por ahora puede mostrarse como texto temporal.
     */
    val illustrationDescription: String? = null,

    /**
     * Permite distinguir visualmente los resultados
     * correspondientes a la noche final.
     */
    val isFinalResult: Boolean = false
)