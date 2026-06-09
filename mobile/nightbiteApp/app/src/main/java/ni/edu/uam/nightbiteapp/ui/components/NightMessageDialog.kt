package ni.edu.uam.nightbiteapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.ui.theme.LavenderGray
import ni.edu.uam.nightbiteapp.ui.theme.NightSurface
import ni.edu.uam.nightbiteapp.ui.theme.SmokeWhite

/**
 * Cuadro superpuesto para mostrar mensajes de confirmación,
 * advertencia, éxito o error dentro de la app.
 *
 * Mantiene una presentación visual consistente:
 * mismo fondo, tipografía, paleta, altura de botones y espaciado.
 */
@Composable
fun NightMessageDialog(
    title: String,
    message: String,
    confirmText: String,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.Warning,
    iconColor: Color = CheeseYellow,
    dismissText: String? = null,
    onDismiss: (() -> Unit)? = null,
    additionalContent: (@Composable () -> Unit)? = null
) {
    Dialog(
        onDismissRequest = {
            onDismiss?.invoke()
        }
    ) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier
                    .offset(y = 30.dp)
                    .size(72.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = CircleShape
                    ),
                shape = CircleShape,
                color = iconColor
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = SmokeWhite,
                    modifier = Modifier.padding(18.dp)
                )
            }

            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = NightSurface
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp
                ),
                modifier = Modifier.widthIn(
                    min = 360.dp,
                    max = 440.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(
                        start = 34.dp,
                        end = 34.dp,
                        top = 52.dp,
                        bottom = 30.dp
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = title,
                        color = SmokeWhite,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = message,
                        color = LavenderGray,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )

                    if (additionalContent != null) {
                        Spacer(modifier = Modifier.height(18.dp))

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            additionalContent()
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    if (dismissText != null && onDismiss != null) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            NightSecondaryButton(
                                text = dismissText,
                                onClick = onDismiss,
                                modifier = Modifier.widthIn(min = 135.dp)
                            )

                            NightPrimaryButton(
                                text = confirmText,
                                onClick = onConfirm,
                                modifier = Modifier.widthIn(min = 135.dp)
                            )
                        }
                    } else {
                        NightPrimaryButton(
                            text = confirmText,
                            onClick = onConfirm,
                            modifier = Modifier.widthIn(min = 190.dp)
                        )
                    }
                }
            }
        }
    }
}