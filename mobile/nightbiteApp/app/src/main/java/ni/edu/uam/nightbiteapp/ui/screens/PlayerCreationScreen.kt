package ni.edu.uam.nightbiteapp.ui.screens

import androidx.compose.runtime.Composable
import ni.edu.uam.nightbiteapp.viewmodel.PlayerCreationViewModel

@Composable
fun PlayerCreationScreen(
    viewModel: PlayerCreationViewModel,
    onPlayerCreated: () -> Unit,
    onBackToHome: () -> Unit
) {
    GenderSelectionScreen(
        viewModel = viewModel,
        onPlayerCreated = onPlayerCreated
    )
}