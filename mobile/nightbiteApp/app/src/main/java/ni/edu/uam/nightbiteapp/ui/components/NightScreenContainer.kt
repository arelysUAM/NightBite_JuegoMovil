package ni.edu.uam.nightbiteapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ni.edu.uam.nightbiteapp.ui.design.NightSpacing
import ni.edu.uam.nightbiteapp.ui.theme.NightBackground

/**
 * Contenedor base para pantallas de NightBite.
 *
 * Centraliza:
 * - fondo principal
 * - tamaño completo
 * - padding estándar
 *
 * Sirve como base para pantallas simples sin imagen de fondo personalizada.
 */
@Composable
fun NightScreenContainer(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = NightSpacing.screenHorizontal,
        vertical = NightSpacing.screenVertical
    ),
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NightBackground)
            .padding(contentPadding),
        content = content
    )
}