package ni.edu.uam.nightbiteapp.ui.validation

import android.util.Patterns

object AccountValidators {

    const val USERNAME_MIN_LENGTH = 4
    const val USERNAME_MAX_LENGTH = 16
    const val EMAIL_MAX_LENGTH = 100
    const val PASSWORD_MIN_LENGTH = 8
    const val PASSWORD_MAX_LENGTH = 20

    fun validateUsername(username: String): String? {
        if (username.isBlank()) {
            return "Usuario obligatorio."
        }

        val normalizedUsername = username.trim()

        if (normalizedUsername.length < USERNAME_MIN_LENGTH) {
            return "Muy corto. Mínimo 4 caracteres."
        }

        if (normalizedUsername.length > USERNAME_MAX_LENGTH) {
            return "Muy largo. Máximo 16 caracteres."
        }

        if (!normalizedUsername.matches(Regex("^[a-z0-9_]+$"))) {
            return "Solo minúsculas, números y guion bajo."
        }

        return null
    }

    fun validateEmail(email: String): String? {
        if (email.isBlank()) {
            return "Correo obligatorio."
        }

        val normalizedEmail = email.trim()

        if (normalizedEmail.contains(" ")) {
            return "No debe contener espacios."
        }

        if (normalizedEmail != normalizedEmail.lowercase()) {
            return "Debe estar en minúsculas."
        }

        if (normalizedEmail.length > EMAIL_MAX_LENGTH) {
            return "Muy largo. Máximo 100 caracteres."
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(normalizedEmail).matches()) {
            return "Correo inválido."
        }

        return null
    }

    fun validatePassword(
        password: String,
        fieldName: String = "Contraseña"
    ): String? {
        if (password.isBlank()) {
            return "$fieldName es obligatoria."
        }

        if (password.length < PASSWORD_MIN_LENGTH) {
            return "Muy corta. Mínimo 8 caracteres."
        }

        if (password.length > PASSWORD_MAX_LENGTH) {
            return "Muy larga. Máximo 20 caracteres."
        }

        if (password.startsWith(" ") || password.endsWith(" ")) {
            return "No puede iniciar o terminar con espacios."
        }

        return null
    }

    fun validateConfirmPassword(
        password: String,
        confirmPassword: String
    ): String? {
        if (confirmPassword.isBlank()) {
            return "Confirma la contraseña."
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
            return "Confirma la nueva contraseña."
        }

        if (newPassword != confirmNewPassword) {
            return "Las contraseñas no coinciden."
        }

        return null
    }
}