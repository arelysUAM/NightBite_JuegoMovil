package ni.edu.uam.nightbiteapp.game

import com.badlogic.gdx.graphics.Color

/**
 * Paleta visual del tutorial en LibGDX.
 *
 * LibGDX usa com.badlogic.gdx.graphics.Color,
 * no androidx.compose.ui.graphics.Color.
 */
object TutorialPalette {

    private fun rgba(hex: Long): Color {
        return Color(hex.toInt())
    }

    // Paleta base NightBite
    val NightBackground = rgba(0x0B1026FF)
    val NightSurface = rgba(0x1A103DFF)
    val NightInnerPurple = rgba(0x21143FFF)

    // Colores principales
    val PizzaRed = rgba(0xFF3B3BFF)
    val CheeseYellow = rgba(0xFFD166FF)
    val NeonCyan = rgba(0x2DE2E6FF)
    val NeonGreen = rgba(0x39FF88FF)

    // Textos y líneas
    val SmokeWhite = rgba(0xF4F4F8FF)
    val LavenderGray = rgba(0xB8B8D1FF)

    // UI / HUD
    val HeaderPurple = rgba(0x5F56CAFF)
    val ActionPurple = rgba(0x3E2EA8FF)
    val PanelBlue = rgba(0x7B92E8FF)
    val PanelBorderBlue = rgba(0x556DCEFF)

    // Mapa
    val RoadGray = rgba(0x5C5C5CFF)
    val RoadDark = rgba(0x2D2D3AFF)
    val BuildingBlue = rgba(0x3E7CA6FF)
    val BuildingLightBlue = rgba(0x9BB3E0FF)
    val ParkGreen = rgba(0x4A753BFF)
    val CemeteryBlue = rgba(0x536DFFFF)

    // Elementos del juego
    val RestaurantRed = PizzaRed
    val DeliveryTarget = CheeseYellow
    val DeliveryPointer = rgba(0xFF4F64FF)
    val Player = NeonCyan
    val OrderPurple = rgba(0x9C27B0FF)

    // Botón pausa
    val PausePink = rgba(0xF28BB3FF)
    val PauseBorder = SmokeWhite
}