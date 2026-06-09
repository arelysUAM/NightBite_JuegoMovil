package ni.edu.uam.nightbiteapp.ui.validation

import android.util.Patterns

object AccountValidators {

    const val USERNAME_MAX_LENGTH = 16
    const val EMAIL_MAX_LENGTH = 100
    const val PASSWORD_MIN_LENGTH = 8
    const val PASSWORD_MAX_LENGTH = 50

    fun validateUsername(username: String): String? {
        if (username.isBlank()) {
            return "El nombre de usuario es obligatorio."
        }

        val normalizedUsername = username.trim()

        if (normalizedUsername.length > USERNAME_MAX_LENGTH) {
            return "El nombre de usuario no debe superar los 16 caracteres."
        }

        if (!normalizedUsername.matches(Regex("^[a-z0-9_]+$"))) {
            return "Caracteres no permitidos. Usa solo letras, números y (_)."
        }

        return null
    }

    fun validateEmail(email: String): String? {
        if (email.isBlank()) {
            return "El correo es obligatorio."
        }

        val normalizedEmail = email.trim()

        if (normalizedEmail.contains(" ")) {
            return "El correo no debe contener espacios."
        }

        if (normalizedEmail != normalizedEmail.lowercase()) {
            return "El correo debe estar en minúsculas."
        }

        if (normalizedEmail.length > EMAIL_MAX_LENGTH) {
            return "El correo no debe superar los 100 caracteres."
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(normalizedEmail).matches()) {
            return "Ingresa un correo electrónico válido."
        }

        return null
    }

    fun validatePassword(
        password: String,
        fieldName: String = "La contraseña"
    ): String? {
        if (password.isBlank()) {
            return "$fieldName es obligatoria"
        }

        if (password.length < PASSWORD_MIN_LENGTH) {
            return "$fieldName debe tener al menos 8 caracteres."
        }

        if (password.length > PASSWORD_MAX_LENGTH) {
            return "$fieldName no debe superar los 50 caracteres."
        }

        if (password.startsWith(" ") || password.endsWith(" ")) {
            return "$fieldName no puede iniciar ni terminar con espacios."
        }

        return null
    }

    fun validateConfirmPassword(
        password: String,
        confirmPassword: String
    ): String? {
        if (confirmPassword.isBlank()) {
            return "La confirmación de contraseña es obligatoria."
        }

        if (password != confirmPassword) {
            return "Las contraseñas no coinciden."
        }

        return null
    }

    fun validateNewPasswordConfirmation(
        newPassword: String,
        confirmNewPassword: String
    ): String? {
        if (confirmNewPassword.isBlank()) {
            return "La confirmación de contraseña es obligatoria."
        }

        if (newPassword != confirmNewPassword) {
            return "La nueva contraseña y la confirmación no coinciden."
        }

        return null
    }
}