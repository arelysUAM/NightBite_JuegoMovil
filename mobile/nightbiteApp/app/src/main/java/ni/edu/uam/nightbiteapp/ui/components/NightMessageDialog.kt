package ni.edu.uam.nightbiteapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import ni.edu.uam.nightbiteapp.ui.design.NightShapes
import ni.edu.uam.nightbiteapp.ui.design.NightSizes
import ni.edu.uam.nightbiteapp.ui.design.NightSpacing
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.ui.theme.LavenderGray
import ni.edu.uam.nightbiteapp.ui.theme.NightSurface
import ni.edu.uam.nightbiteapp.ui.theme.SmokeWhite

/**
 * Cuadro superpuesto para mostrar mensajes de confirmación, advertencia,
 * éxito o error dentro de NightBite.
 *
 * additionalContent permite agregar controles personalizados, como las
 * opciones del menú de pausa.
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
    additionalContent: (@Composable ColumnScope.() -> Unit)? = null
) {
    Dialog(
        onDismissRequest = {
            onDismiss?.invoke()
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            Surface(
                modifier = Modifier
                    .size(NightSizes.iconLarge)
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
                    modifier = Modifier.padding(15.dp)
                )
            }

            Card(
                shape = NightShapes.dialog,
                colors = CardDefaults.cardColors(
                    containerColor = NightSurface
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp
                ),
                modifier = Modifier.width(NightSizes.dialogWidth)
            ) {
                Column(
                    modifier = Modifier.padding(
                        horizontal = NightSpacing.dialogHorizontal,
                        vertical = NightSpacing.dialogVertical
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = title,
                        color = SmokeWhite,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(NightSpacing.small))

                    Text(
                        text = message,
                        color = LavenderGray,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )

                    if (additionalContent != null) {
                        Spacer(modifier = Modifier.height(NightSpacing.large))

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            content = additionalContent
                        )
                    }

                    Spacer(modifier = Modifier.height(NightSpacing.extraLarge))

                    if (dismissText != null && onDismiss != null) {
                        DialogActionsRow(
                            dismissText = dismissText,
                            confirmText = confirmText,
                            onDismiss = onDismiss,
                            onConfirm = onConfirm
                        )
                    } else {
                        NightPrimaryButton(
                            text = confirmText,
                            onClick = onConfirm,
                            modifier = Modifier.width(240.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DialogActionsRow(
    dismissText: String,
    confirmText: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(NightSpacing.medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NightSecondaryButton(
            text = dismissText,
            onClick = onDismiss,
            modifier = Modifier.width(135.dp)
        )

        NightPrimaryButton(
            text = confirmText,
            onClick = onConfirm,
            modifier = Modifier.width(135.dp)
        )
    }
}