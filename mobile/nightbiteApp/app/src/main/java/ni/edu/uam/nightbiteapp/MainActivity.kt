package ni.edu.uam.nightbiteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
        enableEdgeToEdge()

        setContent {
            NightbiteAppTheme {
                AppNavigation()
            }
        }
    }
}