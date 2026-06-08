package ni.edu.uam.nightbiteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import ni.edu.uam.nightbiteapp.navigation.AppNavigation
import ni.edu.uam.nightbiteapp.ui.theme.NightbiteAppTheme

/**
 * Actividad principal de la aplicación móvil NightBite.
 *
 * Inicializa el contenido Compose y carga la navegación principal.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.statusBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        setContent {
            NightbiteAppTheme {
                AppNavigation()
            }
        }
    }
}