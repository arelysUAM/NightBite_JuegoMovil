package ni.edu.uam.nightbiteapp.data.remote.dto

data class UpdatePasswordRequest(
    val currentPassword: String,
    val newPassword: String,
    val confirmNewPassword: String
)