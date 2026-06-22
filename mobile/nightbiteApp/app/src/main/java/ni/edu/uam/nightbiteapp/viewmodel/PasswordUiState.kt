package ni.edu.uam.nightbiteapp.viewmodel

enum class CurrentPasswordStatus {
    IDLE,
    CHECKING,
    VALID,
    INVALID
}

data class PasswordUiState(
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmNewPassword: String = "",

    val currentPasswordError: String? = null,
    val newPasswordError: String? = null,
    val confirmPasswordError: String? = null,

    val currentPasswordVisible: Boolean = false,
    val newPasswordVisible: Boolean = false,
    val confirmPasswordVisible: Boolean = false,

    val currentPasswordStatus: CurrentPasswordStatus = CurrentPasswordStatus.IDLE,

    val showInvalidDataDialog: Boolean = false,
    val showCancelDialog: Boolean = false,
    val showSuccessDialog: Boolean = false,
    val showSaveConfirmationDialog: Boolean = false,

    val isSaving: Boolean = false
) {
    val hasChanges: Boolean
        get() = currentPassword.isNotBlank() ||
                newPassword.isNotBlank() ||
                confirmNewPassword.isNotBlank()

    val currentPasswordIsValid: Boolean
        get() = currentPassword.isNotBlank() &&
                currentPasswordError == null &&
                currentPasswordStatus == CurrentPasswordStatus.VALID

    val newPasswordIsValid: Boolean
        get() = newPassword.isNotBlank() &&
                newPasswordError == null

    val confirmPasswordIsValid: Boolean
        get() = confirmNewPassword.isNotBlank() &&
                confirmPasswordError == null &&
                confirmNewPassword == newPassword &&
                newPasswordIsValid

    val hasVisibleErrors: Boolean
        get() = currentPasswordError != null ||
                newPasswordError != null ||
                confirmPasswordError != null
}