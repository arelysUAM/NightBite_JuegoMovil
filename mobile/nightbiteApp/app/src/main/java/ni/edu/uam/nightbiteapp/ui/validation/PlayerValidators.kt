package ni.edu.uam.nightbiteapp.ui.validation

object PlayerValidators {

    const val DRIVER_NAME_MAX_LENGTH = 80
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

        if (normalizedDriverName.length > DRIVER_NAME_MAX_LENGTH) {
            return "El nombre no debe superar los 80 caracteres."
        }

        if (normalizedDriverName.count { it == ' ' } > 1) {
            return "Ingresa máximo un espacio."
        }

        if (!normalizedDriverName.matches(Regex("^[A-Za-zÁÉÍÓÚáéíóúÑñ]+( [A-Za-zÁÉÍÓÚáéíóúÑñ]+)?$"))) {
            return "El nombre solo puede contener letras."
        }

        return null
    }

    fun validateGender(gender: String): String? {
        if (gender.isBlank()) {
            return "Selecciona un género para continuar."
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
            return "El color del casco no debe superar los 30 caracteres."
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
            return "El tipo de moto no debe superar los 30 caracteres."
        }

        if (!ALLOWED_MOTORCYCLE_TYPES.contains(normalizedMotorcycleType)) {
            return "Tipo de moto no permitido."
        }

        return null
    }

    fun formatPersonName(value: String): String {
        val singleSpacedValue = value
            .replace(Regex("\\s+"), " ")
            .trimStart()

        val limitedSpaceValue = keepOnlyOneSpace(singleSpacedValue)

        return limitedSpaceValue
            .lowercase()
            .split(" ")
            .joinToString(" ") { word ->
                word.replaceFirstChar { char ->
                    if (char.isLowerCase()) {
                        char.titlecase()
                    } else {
                        char.toString()
                    }
                }
            }
    }

    private fun keepOnlyOneSpace(value: String): String {
        val firstSpaceIndex = value.indexOf(' ')

        if (firstSpaceIndex == -1) {
            return value
        }

        val beforeSpace = value.substring(0, firstSpaceIndex + 1)
        val afterSpace = value.substring(firstSpaceIndex + 1).replace(" ", "")

        return beforeSpace + afterSpace
    }
}