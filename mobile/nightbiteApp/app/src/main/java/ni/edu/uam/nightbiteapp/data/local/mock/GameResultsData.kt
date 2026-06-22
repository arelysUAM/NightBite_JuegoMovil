package ni.edu.uam.nightbiteapp.data.local.mock

import ni.edu.uam.nightbiteapp.ui.model.GameResultContent
import ni.edu.uam.nightbiteapp.ui.model.GameResultType

/**
 * Fuente temporal de resultados del juego.
 *
 * Más adelante estos datos podrán combinarse con resultados reales
 * del gameplay y progreso guardado con Room.
 */
object GameResultsData {
    fun getResultContent(
        levelId: Int,
        resultType: GameResultType,
        stars: Int = 0
    ): GameResultContent {
        return when (resultType) {

            GameResultType.TUTORIAL_THREE_STARS -> {
                GameResultContent(
                    title = "Tutorial completado",
                    subtitle = "Desempeño perfecto",
                    message = "Completaste todos los pedidos de tu primera jornada.",
                    details = listOf(
                        "Pedidos completados: 100 %",
                        "Calificación obtenida: 3 estrellas",
                        "La siguiente noche ha sido desbloqueada"
                    ),
                    stars = 3,
                    rewardMessage = "Noche 2 desbloqueada",
                    illustrationDescription = "Tres estrellas sobre la motocicleta del repartidor"
                )
            }

            GameResultType.TUTORIAL_TWO_STARS -> {
                GameResultContent(
                    title = "Tutorial completado",
                    subtitle = "Buen desempeño",
                    message = "Completaste la mayor parte de los pedidos, pero todavía puedes mejorar.",
                    details = listOf(
                        "Pedidos completados: al menos 80 %",
                        "Calificación obtenida: 2 estrellas",
                        "Completa el tutorial con 3 estrellas para desbloquear la siguiente noche"
                    ),
                    stars = 2,
                    illustrationDescription = "Dos estrellas sobre una bolsa de pedidos"
                )
            }

            GameResultType.TUTORIAL_ONE_STAR -> {
                GameResultContent(
                    title = "Tutorial completado",
                    subtitle = "Desempeño suficiente",
                    message = "Lograste completar parte de la jornada, pero quedaron demasiados pedidos pendientes.",
                    details = listOf(
                        "Pedidos completados: al menos 50 %",
                        "Calificación obtenida: 1 estrella",
                        "Completa el tutorial con 3 estrellas para desbloquear la siguiente noche"
                    ),
                    stars = 1,
                    illustrationDescription = "Una estrella sobre una caja de entrega"
                )
            }

            GameResultType.FIRED -> {
                GameResultContent(
                    title = "Despedido",
                    subtitle = "Jornada no completada",
                    message = "No completaste suficientes pedidos durante el tutorial.",
                    details = listOf(
                        "Pedidos completados: menos del 50 %",
                        "Calificación obtenida: 0 estrellas",
                        "El restaurante ha terminado tu contrato"
                    ),
                    stars = 0,
                    illustrationDescription = "Contrato de repartidor marcado como despedido"
                )
            }

            GameResultType.VICTORY -> {
                val safeStars = stars.coerceIn(1, 3)
                val starWord = if (safeStars == 1) {
                    "estrella"
                } else {
                    "estrellas"
                }

                val unlockMessage = if (safeStars == 3) {
                    "La siguiente noche ha sido desbloqueada"
                } else {
                    "Completa la jornada con 3 estrellas para desbloquear la siguiente noche"
                }

                GameResultContent(
                    title = "Jornada completada",
                    subtitle = "Calificación: $safeStars $starWord",
                    message = "Sobreviviste a la noche y completaste la jornada.",
                    details = listOf(
                        "Noche completada: ${levelId + 1}",
                        "Calificación obtenida: $safeStars $starWord",
                        unlockMessage
                    ),
                    stars = safeStars,
                    rewardMessage = if (safeStars == 3) {
                        "Recibiste una nueva medalla"
                    } else {
                        null
                    },
                    illustrationDescription = "Resultado de la jornada del repartidor"
                )
            }

            GameResultType.OUT_OF_LIVES -> {
                GameResultContent(
                    title = "Fin de la jornada",
                    subtitle = "Te quedaste sin vidas",
                    message = "La ciudad quedó en silencio después de tu último intento.",
                    details = listOf(
                        "Noche jugada: ${levelId + 1}",
                        "Vidas restantes: 0",
                        "Calificación obtenida: 0 estrellas"
                    ),
                    stars = 0,
                    illustrationDescription = "Casco del repartidor abandonado bajo un poste de luz"
                )
            }

            GameResultType.INCOMPLETE_DELIVERIES -> {
                GameResultContent(
                    title = "Fin del juego",
                    subtitle = "Entregas incompletas",
                    message = "Sobreviviste, pero no completaste todos los pedidos antes de terminar la jornada.",
                    details = listOf(
                        "Noche jugada: ${levelId + 1}",
                        "Pedidos pendientes: sí",
                        "Calificación obtenida: 0 estrellas"
                    ),
                    stars = 0,
                    illustrationDescription = "Cartel de se busca con la fotografía del repartidor"
                )
            }

            GameResultType.FINAL_VICTORY -> {
                val safeStars = stars.coerceIn(1, 3)
                val starWord = if (safeStars == 1) {
                    "estrella"
                } else {
                    "estrellas"
                }

                GameResultContent(
                    title = "Jornada final completada",
                    subtitle = "Calificación: $safeStars $starWord",
                    message = "Completaste la última jornada y lograste regresar a tu dimensión.",
                    details = listOf(
                        "Noche final completada",
                        "Calificación obtenida: $safeStars $starWord",
                        "Estado del repartidor: libre"
                    ),
                    stars = safeStars,
                    rewardMessage = if (safeStars == 3) {
                        "Recibiste tu paga"
                    } else {
                        null
                    },
                    illustrationDescription = "El repartidor saliendo del restaurante con su pago",
                    isFinalResult = true
                )
            }

            GameResultType.FINAL_DEFEAT -> {
                GameResultContent(
                    title = "Fin de la jornada",
                    subtitle = "Tu reemplazo ha sido asignado",
                    message = "No lograste completar la noche final y quedaste atrapado en esa dimensión.",
                    details = listOf(
                        "Noche final no completada",
                        "Calificación obtenida: 0 estrellas",
                        "Ahora formas parte de los repartidores perdidos"
                    ),
                    stars = 0,
                    illustrationDescription = "El repartidor transformado y encerrado en la dimensión alterna",
                    isFinalResult = true
                )
            }
        }
    }
}