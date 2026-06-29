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
    val NightBackground = rgba(0x07091CFF)
    val NightSurface = rgba(0x15102EFF)
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

    // Mapa / calles
    val MapVoid = rgba(0x090A1EFF)
    val MapPanel = rgba(0x120D2AFF)
    val RoadGlow = rgba(0x282458AA)
    val RoadOuter = rgba(0x1B1933FF)
    val RoadInner = rgba(0x565A6BFF)
    val RoadLine = rgba(0xE7E4F6CC)
    val Sidewalk = rgba(0x8FA7D6FF)

    val RoadDark = RoadOuter
    val RoadGray = RoadInner

    // Edificios / ciudad
    val BuildingBlue = rgba(0x2F6F9DFF)
    val BuildingDarkBlue = rgba(0x204B73FF)
    val BuildingLightBlue = rgba(0x9BB3E0FF)
    val BuildingWindow = rgba(0x68D8FFFF)
    val BuildingWindowOff = rgba(0x23415FFF)

    // Naturaleza / zonas raras
    val ParkGreen = rgba(0x3F6F36FF)
    val ParkDarkGreen = rgba(0x274D27FF)
    val CemeteryBlue = rgba(0x3B48A6FF)
    val CemeteryDark = rgba(0x202454FF)

    // Luces / ambiente
    val LampGlow = rgba(0xFFD16644)
    val CyanGlow = rgba(0x2DE2E633)
    val FogPurple = rgba(0x7B5CFF22)
    val ShadowFog = rgba(0x00000044)

    // Elementos del juego
    val RestaurantRed = PizzaRed
    val RestaurantDarkRed = rgba(0xA5162AFF)
    val DeliveryTarget = CheeseYellow
    val DeliveryPointer = rgba(0xFF4F64FF)
    val Player = NeonCyan
    val OrderPurple = rgba(0x9C27B0FF)

    // Botón pausa
    val PausePink = rgba(0xF28BB3FF)
    val PauseBorder = SmokeWhite
}