package ni.edu.uam.nightbiteapp.ui.components.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ni.edu.uam.nightbiteapp.ui.design.NightShapes
import ni.edu.uam.nightbiteapp.ui.design.NightSpacing
import ni.edu.uam.nightbiteapp.ui.theme.NightSurface
import ni.edu.uam.nightbiteapp.ui.theme.SmokeWhite

/**
 * Card base reutilizable para secciones visuales de NightBite.
 *
 * Sirve como contenedor estándar para:
 * - información de enemigos
 * - resultados de jornadas
 * - datos de cuenta
 * - logros
 * - inventario
 * - contenido narrativo
 */
@Composable
fun NightBaseCard(
    modifier: Modifier = Modifier,
    fillMaxWidth: Boolean = true,
    containerColor: Color = NightSurface,
    contentColor: Color = SmokeWhite,
    borderColor: Color? = null,
    elevation: Dp = 4.dp,
    contentPadding: PaddingValues = PaddingValues(NightSpacing.large),
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = if (fillMaxWidth) {
            modifier.fillMaxWidth()
        } else {
            modifier
        },
        shape = NightShapes.dialog,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        border = borderColor?.let {
            BorderStroke(
                width = 1.5.dp,
                color = it
            )
        },
        elevation = CardDefaults.cardElevation(
            defaultElevation = elevation
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding),
            content = content
        )
    }
}