package ni.edu.uam.nightbiteapp.ui.validation

import android.util.Patterns

/**
 * Contiene todas las validaciones locales del formulario de registro.
 *
 * Importante:
 * Cada función devuelve:
 *
 * - null -> campo válido
 * - String -> mensaje de error
 *
 * Cuando existen varios errores simultáneos,
 * solamente se devuelve el primero encontrado
 * según el orden de validación definido.
 */
object RegisterValidators {

    /**
     * Valida el nombre de usuario.
     *
     * Reglas:
     * - Obligatorio
     * - Sin espacios
     * - Sin mayúsculas
     * - Solo minúsculas, números y _
     * - Máximo 16 caracteres
     */
    fun validateUsername(
        username: String
    ): String? {

        if (username.isBlank()) {
            return "El nombre de usuario es obligatorio."
        }

        if (username.contains(" ")) {
            return "El nombre de usuario no puede tener espacios."
        }

        if (username.any { it.isUpperCase() }) {
            return "El nombre de usuario no puede tener letras mayúsculas."
        }

        if (!username.matches(Regex("^[a-z0-9_]+$"))) {
            return "El nombre de usuario solo puede usar minúsculas, números y guion bajo (_)."
        }

        if (username.length > 16) {
            return "El nombre de usuario no puede tener más de 16 caracteres."
        }

        return null
    }

    /**
     * Valida el correo electrónico.
     *
     * Reglas:
     * - Obligatorio
     * - Sin espacios
     * - Formato válido
     */
    fun validateEmail(
        email: String
    ): String? {

        if (email.isBlank()) {
            return "El correo electrónico es obligatorio."
        }

        if (email.contains(" ")) {
            return "El correo no puede tener espacios."
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "Ingresa un correo electrónico válido."
        }

        return null
    }

    /**
     * Valida la contraseña.
     *
     * Reglas:
     * - Obligatoria
     * - Mínimo 8 caracteres
     * - Sin espacios al inicio o final
     * - Máximo 15 caracteres
     */
    fun validatePassword(
        password: String
    ): String? {

        if (password.isBlank()) {
            return "La contraseña es obligatoria."
        }

        if (password.length < 8) {
            return "La contraseña debe tener al menos 8 caracteres."
        }

        if (
            password.startsWith(" ") ||
            password.endsWith(" ")
        ) {
            return "La contraseña no puede iniciar ni terminar con espacios."
        }

        if (password.length > 15) {
            return "La contraseña no puede tener más de 15 caracteres."
        }

        return null
    }

    /**
     * Valida la confirmación de contraseña.
     *
     * Reglas:
     * - Obligatoria
     * - Debe coincidir con la contraseña
     */
    fun validateConfirmPassword(
        password: String,
        confirmPassword: String
    ): String? {

        if (confirmPassword.isBlank()) {
            return "Confirma tu contraseña."
        }

        if (password != confirmPassword) {
            return "Las contraseñas no coinciden."
        }

        return null
    }
}