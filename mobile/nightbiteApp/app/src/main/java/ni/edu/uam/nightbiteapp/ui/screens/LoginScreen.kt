package ni.edu.uam.nightbiteapp.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ni.edu.uam.nightbiteapp.ui.components.NightLoginCard

/**
 * Pantalla visual de inicio de sesión.
 *
 * Permite al jugador ingresar con sus credenciales o dirigirse
 * a la pantalla de registro para crear una cuenta.
 */
@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onNavigateToHome: () -> Unit,
    onExitApp: () -> Unit
) {
    val context = LocalContext.current

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var lastBackPressTime by remember { mutableLongStateOf(0L) }

    BackHandler {
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastBackPressTime < 2000) {
            onExitApp()
        } else {
            lastBackPressTime = currentTime
            Toast.makeText(
                context,
                "Presiona nuevamente para salir",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B1026)),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "NIGHTBITE",
                    color = Color(0xFFFFD166),
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Black
                )

                Text(
                    text = "Delivery nocturno",
                    color = Color(0xFFB8B8D1),
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.width(24.dp))

                Text(
                    text = "Completa tu turno.\nEvita lo que camina de noche.",
                    color = Color(0xFFF8F5E9),
                    fontSize = 14.sp
                )
            }

            NightLoginCard(
                username = username,
                password = password,
                onUsernameChange = { username = it },
                onPasswordChange = { password = it },
                onLoginClick = onNavigateToHome,
                onRegisterClick = onNavigateToRegister
            )
        }
    }
}