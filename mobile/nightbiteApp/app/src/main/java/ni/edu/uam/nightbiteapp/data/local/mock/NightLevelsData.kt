package ni.edu.uam.nightbiteapp.data.local.mock

import ni.edu.uam.nightbiteapp.ui.model.NightLevel

object NightLevelsData {

    val levels = listOf(
        NightLevel(
            id = 0,
            title = "Noche 0",
            subtitle = "Tutorial",
            narrativeMessage = "Primera jornada. La ciudad parece normal, los pedidos llegan a tiempo y nada parece estar fuera de lugar.",
            enemyName = "Sin enemigo",
            enemyDescription = "Esta noche sirve para aprender el ciclo básico de entregas.",
            enemyBehavior = "No hay criaturas activas durante el tutorial.",
            survivalTip = "Aprende a recoger pedidos, entregarlos y regresar a la pizzería antes de que termine el tiempo."
        ),
        NightLevel(
            id = 1,
            title = "Noche 1",
            subtitle = "Sombras errantes",
            narrativeMessage = "Se detectó actividad inusual en la zona. Evite el contacto visual prolongado.",
            enemyName = "Sombra errante",
            enemyDescription = "Figuras oscuras con ojos brillantes que parecen peatones perdidos en la calle.",
            enemyBehavior = "Se mueven lentamente y patrullan rutas simples.",
            survivalTip = "Evita esquinas cerradas y mantén distancia cuando crucen tu ruta."
        ),
        NightLevel(
            id = 2,
            title = "Noche 2",
            subtitle = "Perros espectrales",
            narrativeMessage = "Los animales ya notaron tu presencia.",
            enemyName = "Perro espectral",
            enemyDescription = "Perros pequeños o medianos con ojos brillantes y movimientos repentinos.",
            enemyBehavior = "Se desplazan más rápido y pueden realizar embestidas cortas.",
            survivalTip = "Evita trayectorias rectas largas y cambia de ruta cuando se acerquen."
        ),
        NightLevel(
            id = 3,
            title = "Noche 3",
            subtitle = "Clientes deformes",
            narrativeMessage = "Algunos clientes no distinguen entre repartidores y pedidos.",
            enemyName = "Cliente deforme",
            enemyDescription = "Figuras humanoides que parecen esperar en silencio hasta que te acercas demasiado.",
            enemyBehavior = "Permanecen inmóviles al inicio y se activan cuando entras en su rango.",
            survivalTip = "No confíes en figuras quietas cerca de las zonas de entrega."
        ),
        NightLevel(
            id = 4,
            title = "Noche 4",
            subtitle = "Repartidores perdidos",
            narrativeMessage = "No todos los repartidores terminan su turno.",
            enemyName = "Repartidor perdido",
            enemyDescription = "Antiguos repartidores que nunca lograron salir de la dimensión alterna.",
            enemyBehavior = "Patrullan rutas clave y persiguen con mayor precisión.",
            survivalTip = "No tomes siempre el camino más corto. Ellos conocen el mapa."
        )
    )

    fun getLevelById(levelId: Int): NightLevel? {
        return levels.find { it.id == levelId }
    }
}