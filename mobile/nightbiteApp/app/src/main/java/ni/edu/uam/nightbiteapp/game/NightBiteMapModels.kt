package ni.edu.uam.nightbiteapp.game

/**
 * Segmento visual de calle.
 *
 * Se usa para dibujar las calles entre nodos conectados.
 */
data class NightBiteStreetSegment(
    val startX: Float,
    val startY: Float,
    val endX: Float,
    val endY: Float
)

/**
 * Tipo de decoración del mapa.
 */
enum class NightBiteDecorationType {
    BUILDING,
    PARK,
    CEMETERY
}

/**
 * Rectángulo decorativo del mapa.
 */
data class NightBiteMapDecoration(
    val id: String,
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
    val type: NightBiteDecorationType
)

/*
 * Alias temporales para que los archivos que aún usan nombres viejos
 * sigan compilando mientras terminamos la migración.
 */
typealias TutorialStreetSegment = NightBiteStreetSegment
typealias TutorialDecorationType = NightBiteDecorationType
typealias TutorialMapDecoration = NightBiteMapDecoration