package ni.edu.uam.nightbiteapp.ui.validation

object PlayerValidators {

    const val DRIVER_NAME_MIN_LENGTH = 3
    const val DRIVER_NAME_MAX_LENGTH = 20
    const val OPTION_MAX_LENGTH = 30

    const val DEFAULT_HELMET_COLOR = "Negro"
    const val DEFAULT_MOTORCYCLE_TYPE = "Delivery"

    val ALLOWED_HELMET_COLORS = listOf(
        "Negro",
        "Rojo",
        "Azul",
        "Blanco",
        "Amarillo"
    )

    val ALLOWED_MOTORCYCLE_TYPES = listOf(
        "Estándar",
        "Scooter",
        "Deportiva",
        "Retro",
        "Delivery"
    )

    fun validateDriverName(driverName: String): String? {
        if (driverName.isBlank()) {
            return "El nombre es obligatorio."
        }

        val normalizedDriverName = driverName.trim()

        if (normalizedDriverName.length < DRIVER_NAME_MIN_LENGTH) {
            return "El nombre debe tener mínimo 3 letras."
        }

        if (normalizedDriverName.length > DRIVER_NAME_MAX_LENGTH) {
            return "El nombre no debe superar 20 caracteres."
        }

        if (normalizedDriverName.count { it == ' ' } > 1) {
            return "Usa solo nombre o nombre y apellido."
        }

        if (!normalizedDriverName.matches(Regex("^[A-Za-zÁÉÍÓÚáéíóúÑñ]+( [A-Za-zÁÉÍÓÚáéíóúÑñ]+)?$"))) {
            return "Solo se permiten letras."
        }

        return null
    }

    fun validateGender(gender: String): String? {
        if (gender.isBlank()) {
            return "El género es obligatorio."
        }

        if (gender != "Femenino" && gender != "Masculino") {
            return "El género seleccionado no es válido."
        }

        return null
    }

    fun validateHelmetColor(helmetColor: String): String? {
        if (helmetColor.isBlank()) {
            return "El color del casco es obligatorio."
        }

        val normalizedHelmetColor = helmetColor.trim()

        if (normalizedHelmetColor.length > OPTION_MAX_LENGTH) {
            return "El color del casco no debe superar 30 caracteres."
        }

        if (!ALLOWED_HELMET_COLORS.contains(normalizedHelmetColor)) {
            return "Color de casco no permitido."
        }

        return null
    }

    fun validateMotorcycleType(motorcycleType: String): String? {
        if (motorcycleType.isBlank()) {
            return "El tipo de moto es obligatorio."
        }

        val normalizedMotorcycleType = motorcycleType.trim()

        if (normalizedMotorcycleType.length > OPTION_MAX_LENGTH) {
            return "El tipo de moto no debe superar 30 caracteres."
        }

        if (!ALLOWED_MOTORCYCLE_TYPES.contains(normalizedMotorcycleType)) {
            return "Tipo de moto no permitido."
        }

        return null
    }

    fun formatPersonName(value: String): String {
        val cleanedValue = value
            .replace(Regex("[^A-Za-zÁÉÍÓÚáéíóúÑñ ]"), "")
            .replace(Regex("\\s+"), " ")
            .trimStart()

        val limitedWordsValue = keepOnlyTwoWords(cleanedValue)

        val formattedValue = limitedWordsValue
            .split(" ")
            .joinToString(" ") { word ->
                word.lowercase().replaceFirstChar { char ->
                    if (char.isLowerCase()) {
                        char.titlecase()
                    } else {
                        char.toString()
                    }
                }
            }

        return formattedValue
            .take(DRIVER_NAME_MAX_LENGTH)
            .trimEnd()
    }

    private fun keepOnlyTwoWords(value: String): String {
        val words = value
            .split(" ")
            .filter { it.isNotBlank() }
            .take(2)

        val endsWithSpace = value.endsWith(" ") && words.size < 2

        return buildString {
            append(words.joinToString(" "))

            if (endsWithSpace && isNotBlank()) {
                append(" ")
            }
        }
    }
}