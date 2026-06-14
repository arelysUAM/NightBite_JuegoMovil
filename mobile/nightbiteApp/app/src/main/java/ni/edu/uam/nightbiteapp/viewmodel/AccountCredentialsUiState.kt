package ni.edu.uam.nightbiteapp.viewmodel

data class AccountCredentialsUiState(
    val newUsername: String = "",
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmNewPassword: String = "",

    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val showDeleteAccountDialog: Boolean = false,
    val showAccountDeletedDialog: Boolean = false,

    val showConfirmDialog: Boolean = false,
    val showSessionExpiredDialog: Boolean = false
)