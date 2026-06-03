package ni.edu.uam.nightbiteapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ni.edu.uam.nightbiteapp.data.remote.dto.UserResponse

/**
 * Pantalla de perfil del usuario.
 *
 * Muestra datos no sensibles de la cuenta y la ficha Player asignada.
 */
@Composable
fun ProfileScreen(
    user: UserResponse?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Perfil",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (user == null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = "No hay datos de usuario cargados.",
                    modifier = Modifier.padding(16.dp)
                )
            }
            return@Column
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(PaddingValues(16.dp))
            ) {
                Text(
                    text = "Datos de la cuenta",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "ID: ${user.id}")
                Text(text = "Usuario: ${user.username}")
                Text(text = "Correo: ${user.email}")
                Text(text = "Edad: ${user.age}")
                Text(text = "Creado el: ${user.createdAt ?: "No disponible"}")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(PaddingValues(16.dp))
            ) {
                Text(
                    text = "Ficha del repartidor",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                val player = user.player

                if (player == null) {
                    Text(text = "Aún no ha creado su player")
                } else {
                    Text(text = "ID Player: ${player.id}")
                    Text(text = "Apodo: ${player.nickname}")
                    Text(text = "Nombre del repartidor: ${player.driverName}")
                    Text(text = "Género: ${player.gender}")
                    Text(text = "Color de casco: ${player.helmetColor}")
                    Text(text = "Tipo de moto: ${player.motorcycleType}")
                }
            }
        }
    }
}