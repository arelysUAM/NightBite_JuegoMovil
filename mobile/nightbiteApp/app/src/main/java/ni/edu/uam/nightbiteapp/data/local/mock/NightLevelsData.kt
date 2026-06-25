package ni.edu.uam.nightbiteapp.data.local.mock

import ni.edu.uam.nightbiteapp.R
import ni.edu.uam.nightbiteapp.ui.model.LevelIntroContent
import ni.edu.uam.nightbiteapp.ui.model.NightLevel

object NightLevelsData {

    val levels = listOf(
        NightLevel(
            id = 0,
            title = "Noche 0",
            subtitle = "Tutorial",
            narrativeMessage = "Primera jornada. La ciudad parece normal, los pedidos llegan a tiempo y nada parece estar fuera de lugar.",
            introContent = LevelIntroContent(
                enemyImageRes = R.drawable.sombras_lv2,
                enemyTitle = "Actividad desconocida",
                alertMessage = "La noche presenta anomalías. Mantente alerta.",
                difficulty = "Desconocida",
                behavior = "No se ha identificado un patrón estable.",
                tip = "Avanza con cautela y prioriza salir de la zona."
            ),
            isUnlocked = true
        ),
        NightLevel(
            id = 1,
            title = "Noche 1",
            subtitle = "Sombras errantes",
            narrativeMessage = "Se detectó actividad inusual en la zona. Evite el contacto visual prolongado.",
            introContent = LevelIntroContent(
                enemyImageRes = R.drawable.sombras_lv2,
                enemyTitle = "Sombras errantes",
                alertMessage = "Se detectó actividad inusual en la zona. Evita el contacto visual prolongado.",
                difficulty = "Baja",
                behavior = "Siguen rutas fijas en zonas oscuras. Pueden bloquear caminos si no calculas bien tu ruta.",
                tip = "Observa su recorrido antes de avanzar. Evita calles con poca luz y no improvises giros innecesarios."
            ),
            isUnlocked = false
        ),
        NightLevel(
            id = 2,
            title = "Noche 2",
            subtitle = "Lobos callejeros",
            narrativeMessage = "Se oyen ladridos cerca de la ruta. Si escuchas garras, acelera.",
            introContent = LevelIntroContent(
                enemyImageRes = R.drawable.lobos_lv3,
                enemyTitle = "Lobos callejeros",
                alertMessage = "Se oyen ladridos cerca de la ruta. Si escuchas garras, acelera.",
                difficulty = "Media",
                behavior = "Parecen patrullar las calles. Son rápidos y no se cansan fácil.",
                tip = "No te acerques demasiado y evita calles sin salida. Si uno te detecta, busca una ruta abierta para escapar."
            ),
            isUnlocked = false
        ),
        NightLevel(
            id = 3,
            title = "Noche 3",
            subtitle = "Clientes deformes",
            narrativeMessage = "Algunos clientes son demasiado impacientes para esperar por su pedido, así que salen por él.",
            introContent = LevelIntroContent(
                enemyImageRes = R.drawable.deformes_lv4,
                enemyTitle = "Clientes deformes",
                alertMessage = "Algunos clientes son demasiado impacientes para esperar por su pedido, así que salen por él.",
                difficulty = "Alta",
                behavior = "Permanecen quietos hasta que entras en su rango. No te acerques demasiado.",
                tip = "Entrega rápido y mantén distancia. Si uno comienza a moverse, no te quedes cerca."
            ),
            isUnlocked = false
        ),
        NightLevel(
            id = 4,
            title = "Noche 4",
            subtitle = "Repartidores perdidos",
            narrativeMessage = "Si no completas tu jornada, podrías terminar trabajando para esta dimensión igual que ellos.",
            introContent = LevelIntroContent(
                enemyImageRes = R.drawable.repartidores_lv5,
                enemyTitle = "Repartidores perdidos",
                alertMessage = "Si no completas tu jornada, podrías terminar trabajando para esta dimensión igual que ellos.",
                difficulty = "Muy alta",
                behavior = "Intentan quitarte los pedidos para entregarlos ellos, aferrados a la esperanza de volver a casa.",
                tip = "Protege tus pedidos y no entres en su ruta. Si se acercan demasiado, cambia de dirección antes de que te alcancen."
            ),
            isUnlocked = false
        )
    )

    fun getLevelById(levelId: Int): NightLevel? {
        return levels.find { level ->
            level.id == levelId
        }
    }

    fun getLevelsByProgress(maxUnlockedLevelId: Int): List<NightLevel> {
        return levels.map { level ->
            level.copy(
                isUnlocked = level.id <= maxUnlockedLevelId
            )
        }
    }
}