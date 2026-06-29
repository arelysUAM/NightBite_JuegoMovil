package ni.edu.uam.nightbiteapp.game

/**
 * Tipo de enemigo activo en una jornada.
 *
 * Por ahora algunos niveles pueden no tener enemigo implementado,
 * pero la configuración queda preparada para agregarlos sin tocar el mapa.
 */
enum class NightBiteEnemyType {
    NONE,
    WANDERING_SHADOWS,
    SPECTRAL_DOGS,
    DEFORMED_CLIENTS,
    LOST_DELIVERY_RIDERS
}

/**
 * Configuración jugable de una jornada.
 *
 * El mapa puede ser el mismo para todos los niveles, pero cada jornada
 * puede cambiar la cantidad de pedidos, destinos, tiempo y enemigos.
 */
data class GameLevelConfig(
    val levelId: Int,
    val title: String,
    val totalOrders: Int,
    val timeLimitSeconds: Float,
    val deliveryNodeIds: List<String>,
    val enemyType: NightBiteEnemyType
)

/**
 * Configuraciones oficiales de las jornadas de NightBite.
 */
object NightBiteLevelConfigs {

    val tutorial = GameLevelConfig(
        levelId = 0,
        title = "NightBite - Tutorial",
        totalOrders = 8,
        timeLimitSeconds = 7f,
        deliveryNodeIds = listOf(
            "delivery_north_west",
            "delivery_north_east",
            "delivery_west",
            "delivery_east",
            "delivery_south_west",
            "delivery_south_east",
            "delivery_bottom_west",
            "delivery_bottom_east"
        ),
        enemyType = NightBiteEnemyType.NONE
    )

    val levelOne = GameLevelConfig(
        levelId = 1,
        title = "Jornada 1 - Sombras errantes",
        totalOrders = 6,
        timeLimitSeconds = 8f,
        deliveryNodeIds = listOf(
            "delivery_north_west",
            "delivery_east",
            "delivery_south_west",
            "delivery_bottom_east",
            "delivery_west",
            "delivery_north_east"
        ),
        enemyType = NightBiteEnemyType.WANDERING_SHADOWS
    )

    val levelTwo = GameLevelConfig(
        levelId = 2,
        title = "Jornada 2 - Perros espectrales",
        totalOrders = 7,
        timeLimitSeconds = 8f,
        deliveryNodeIds = listOf(
            "delivery_south_east",
            "delivery_bottom_west",
            "delivery_north_east",
            "delivery_west",
            "delivery_east",
            "delivery_south_west",
            "delivery_north_west"
        ),
        enemyType = NightBiteEnemyType.SPECTRAL_DOGS
    )

    val levelThree = GameLevelConfig(
        levelId = 3,
        title = "Jornada 3 - Clientes deformes",
        totalOrders = 8,
        timeLimitSeconds = 7f,
        deliveryNodeIds = listOf(
            "delivery_east",
            "delivery_north_east",
            "delivery_south_east",
            "delivery_bottom_east",
            "delivery_west",
            "delivery_north_west",
            "delivery_south_west",
            "delivery_bottom_west"
        ),
        enemyType = NightBiteEnemyType.DEFORMED_CLIENTS
    )

    val levelFour = GameLevelConfig(
        levelId = 4,
        title = "Jornada 4 - Repartidores perdidos",
        totalOrders = 9,
        timeLimitSeconds = 6f,
        deliveryNodeIds = listOf(
            "delivery_north_west",
            "delivery_south_east",
            "delivery_bottom_west",
            "delivery_north_east",
            "delivery_west",
            "delivery_bottom_east",
            "delivery_south_west",
            "delivery_east",
            "delivery_north_west"
        ),
        enemyType = NightBiteEnemyType.LOST_DELIVERY_RIDERS
    )

    val allLevels = listOf(
        tutorial,
        levelOne,
        levelTwo,
        levelThree,
        levelFour
    )

    fun getByLevelId(levelId: Int): GameLevelConfig {
        return allLevels.firstOrNull { it.levelId == levelId } ?: tutorial
    }
}