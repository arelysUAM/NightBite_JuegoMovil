package ni.edu.uam.nightbiteapp.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.DriveEta
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import ni.edu.uam.nightbiteapp.ui.components.NightBaseCard
import ni.edu.uam.nightbiteapp.ui.components.NightMessageDialog
import ni.edu.uam.nightbiteapp.ui.components.NightPrimaryButton
import ni.edu.uam.nightbiteapp.ui.components.NightSecondaryButton
import ni.edu.uam.nightbiteapp.ui.components.NightTextField
import ni.edu.uam.nightbiteapp.ui.design.NightShapes
import ni.edu.uam.nightbiteapp.ui.design.NightSizes
import ni.edu.uam.nightbiteapp.ui.design.NightSpacing
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.ui.theme.DarkText
import ni.edu.uam.nightbiteapp.ui.theme.FieldBackground
import ni.edu.uam.nightbiteapp.ui.theme.LavenderGray
import ni.edu.uam.nightbiteapp.ui.theme.NeonGreen
import ni.edu.uam.nightbiteapp.ui.theme.NightSurface
import ni.edu.uam.nightbiteapp.ui.theme.PizzaRed
import ni.edu.uam.nightbiteapp.viewmodel.PlayerCreationViewModel

@Composable
fun PlayerCreationScreen(
    viewModel: PlayerCreationViewModel,
    onPlayerCreated: () -> Unit,
    onBackToHome: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var showExitDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(uiState.isPlayerCreated) {
        if (uiState.isPlayerCreated) {
            onPlayerCreated()
        }
    }

    fun requestExit() {
        if (!uiState.isLoading) {
            showExitDialog = true
        }
    }

    BackHandler {
        requestExit()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = NightSpacing.screenHorizontal,
                vertical = NightSpacing.screenVertical
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PlayerCreationHeader()

        Spacer(modifier = Modifier.height(NightSpacing.extraLarge))

        PlayerCreationForm(
            nickname = uiState.nickname,
            driverName = uiState.driverName,
            gender = uiState.gender,
            genderOptions = viewModel.genderOptions,
            helmetColor = uiState.helmetColor,
            helmetColorOptions = viewModel.helmetColorOptions,
            motorcycleType = uiState.motorcycleType,
            motorcycleTypeOptions = viewModel.motorcycleTypeOptions,
            isLoading = uiState.isLoading,
            onNicknameChange = viewModel::onNicknameChange,
            onDriverNameChange = viewModel::onDriverNameChange,
            onGenderSelected = viewModel::onGenderSelected,
            onHelmetColorSelected = viewModel::onHelmetColorSelected,
            onMotorcycleTypeSelected = viewModel::onMotorcycleTypeSelected,
            onCreatePlayer = viewModel::createPlayer,
            onBack = {
                requestExit()
            }
        )
    }

    PlayerCreationDialogs(
        errorMessage = uiState.errorMessage,
        showExitDialog = showExitDialog,
        onDismissError = {
            viewModel.clearError()
        },
        onConfirmExit = {
            showExitDialog = false
            onBackToHome()
        },
        onDismissExit = {
            showExitDialog = false
        }
    )
}

@Composable
private fun PlayerCreationHeader() {
    Text(
        text = "Ficha de Contratación",
        style = MaterialTheme.typography.headlineSmall
    )

    Spacer(modifier = Modifier.height(NightSpacing.small))

    Text(
        text = "Antes de iniciar tu jornada, completa los datos básicos del repartidor asignado.",
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
private fun PlayerCreationForm(
    nickname: String,
    driverName: String,
    gender: String,
    genderOptions: List<String>,
    helmetColor: String,
    helmetColorOptions: List<String>,
    motorcycleType: String,
    motorcycleTypeOptions: List<String>,
    isLoading: Boolean,
    onNicknameChange: (String) -> Unit,
    onDriverNameChange: (String) -> Unit,
    onGenderSelected: (String) -> Unit,
    onHelmetColorSelected: (String) -> Unit,
    onMotorcycleTypeSelected: (String) -> Unit,
    onCreatePlayer: () -> Unit,
    onBack: () -> Unit
) {
    NightBaseCard(
        modifier = Modifier.widthIn(
            max = NightSizes.settingsPanelMaxWidth
        ),
        contentPadding = PaddingValues(
            horizontal = NightSpacing.extraLarge,
            vertical = NightSpacing.extraLarge
        )
    ) {
        ContractSummaryCard()

        Spacer(modifier = Modifier.height(NightSpacing.extraLarge))

        NightTextField(
            value = nickname,
            onValueChange = onNicknameChange,
            label = "Apodo del repartidor",
            leadingIcon = Icons.Default.Badge,
            enabled = !isLoading,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(NightSpacing.medium))

        NightTextField(
            value = driverName,
            onValueChange = onDriverNameChange,
            label = "Nombre del repartidor",
            leadingIcon = Icons.Default.Person,
            enabled = !isLoading,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(NightSpacing.extraLarge))

        GenderSelector(
            selectedGender = gender,
            options = genderOptions,
            enabled = !isLoading,
            onGenderSelected = onGenderSelected
        )

        Spacer(modifier = Modifier.height(NightSpacing.extraLarge))

        PlayerDropdownField(
            label = "Color de casco",
            selectedValue = helmetColor,
            options = helmetColorOptions,
            enabled = !isLoading,
            onOptionSelected = onHelmetColorSelected
        )

        Spacer(modifier = Modifier.height(NightSpacing.medium))

        PlayerDropdownField(
            label = "Tipo de moto",
            selectedValue = motorcycleType,
            options = motorcycleTypeOptions,
            enabled = !isLoading,
            onOptionSelected = onMotorcycleTypeSelected
        )

        Spacer(modifier = Modifier.height(NightSpacing.extraLarge))

        NightPrimaryButton(
            text = "Crear ficha de repartidor",
            onClick = onCreatePlayer,
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        )

        if (isLoading) {
            Spacer(modifier = Modifier.height(NightSpacing.medium))

            CircularProgressIndicator(
                color = CheeseYellow,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        Spacer(modifier = Modifier.height(NightSpacing.medium))

        NightSecondaryButton(
            text = "Volver",
            onClick = onBack,
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ContractSummaryCard() {
    NightBaseCard(
        containerColor = NightSurface,
        borderColor = CheeseYellow.copy(alpha = 0.55f),
        elevation = 2.dp,
        contentPadding = PaddingValues(NightSpacing.large)
    ) {
        Text(
            text = "Puesto: Delivery nocturno",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(NightSpacing.small))

        Text(text = "Horario: 12:00 a.m. - 3:00 a.m.")
        Text(text = "Restaurante: NightBite Pizza")
        Text(text = "Estado: Pendiente de asignación")
    }
}

@Composable
private fun GenderSelector(
    selectedGender: String,
    options: List<String>,
    enabled: Boolean,
    onGenderSelected: (String) -> Unit
) {
    Text(
        text = "Género",
        style = MaterialTheme.typography.titleMedium
    )

    Spacer(modifier = Modifier.height(NightSpacing.small))

    options.forEach { option ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .selectable(
                    selected = selectedGender == option,
                    enabled = enabled,
                    onClick = {
                        onGenderSelected(option)
                    },
                    role = Role.RadioButton
                )
                .padding(vertical = NightSpacing.extraSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selectedGender == option,
                enabled = enabled,
                onClick = {
                    onGenderSelected(option)
                }
            )

            Spacer(modifier = Modifier.width(NightSpacing.small))

            Text(text = option)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlayerDropdownField(
    label: String,
    selectedValue: String,
    options: List<String>,
    enabled: Boolean,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            if (enabled) {
                expanded = !expanded
            }
        }
    ) {
        OutlinedTextField(
            value = selectedValue,
            onValueChange = {},
            enabled = enabled,
            readOnly = true,
            label = {
                Text(text = label)
            },
            leadingIcon = {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.DriveEta,
                    contentDescription = label
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            shape = NightShapes.textField,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = FieldBackground,
                unfocusedContainerColor = FieldBackground,
                disabledContainerColor = FieldBackground,
                focusedIndicatorColor = CheeseYellow,
                unfocusedIndicatorColor = CheeseYellow,
                disabledIndicatorColor = LavenderGray,
                focusedTextColor = DarkText,
                unfocusedTextColor = DarkText,
                disabledTextColor = DarkText.copy(alpha = 0.55f),
                focusedLabelColor = NightSurface,
                unfocusedLabelColor = LavenderGray,
                disabledLabelColor = LavenderGray.copy(alpha = 0.65f),
                focusedLeadingIconColor = NightSurface,
                unfocusedLeadingIconColor = NightSurface,
                disabledLeadingIconColor = LavenderGray,
                focusedTrailingIconColor = NightSurface,
                unfocusedTrailingIconColor = NightSurface,
                disabledTrailingIconColor = LavenderGray,
                cursorColor = CheeseYellow
            ),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .height(NightSizes.textFieldHeight)
                .clickable {
                    if (enabled) {
                        expanded = true
                    }
                }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(text = option)
                    },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun PlayerCreationDialogs(
    errorMessage: String?,
    showExitDialog: Boolean,
    onDismissError: () -> Unit,
    onConfirmExit: () -> Unit,
    onDismissExit: () -> Unit
) {
    if (errorMessage != null) {
        NightMessageDialog(
            title = "No se pudo continuar",
            message = errorMessage,
            confirmText = "Entendido",
            dismissText = null,
            icon = Icons.Default.Warning,
            iconColor = CheeseYellow,
            onConfirm = onDismissError,
            onDismiss = onDismissError
        )
    }

    if (showExitDialog) {
        NightMessageDialog(
            title = "Salir sin crear ficha",
            message = "Todavía no has creado tu ficha de repartidor. ¿Deseas volver al menú principal sin completar la ficha?",
            confirmText = "Sí, volver",
            dismissText = "Cancelar",
            icon = Icons.Default.Warning,
            iconColor = CheeseYellow,
            onConfirm = onConfirmExit,
            onDismiss = onDismissExit
        )
    }
}