package ni.edu.uam.nightbiteapp.viewmodel

data class AccountCredentialsUiState(
    val username: String = "",
    val email: String = "",
    val age: String = "",
    val gender: String = "",
    val createdAt: String = "",

    val originalUsername: String = "",
    val originalAge: String = "",
    val originalGender: String = "",

    val usernameError: String? = null,
    val ageError: String? = null,
    val genderError: String? = null,

    val isEditing: Boolean = false,
    val isLoading: Boolean = false,

    val errorMessage: String? = null,
    val successMessage: String? = null,

    val showSaveConfirmationDialog: Boolean = false,
    val showChangesSavedDialog: Boolean = false,
    val showCancelConfirmationDialog: Boolean = false,
    val showExitConfirmationDialog: Boolean = false,
    val showResetProgressDialog: Boolean = false,
    val showInvalidDataDialog: Boolean = false,
    val showProgressResetSuccessDialog: Boolean = false,

    val showDeleteAccountDialog: Boolean = false,
    val showAccountDeletedDialog: Boolean = false,

    // Campos antiguos, se dejan temporalmente para evitar errores
    // mientras terminamos de reemplazar el ViewModel y la pantalla.
    val newUsername: String = "",
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmNewPassword: String = "",
    val showConfirmDialog: Boolean = false,
    val showCredentialsUpdatedDialog: Boolean = false
)