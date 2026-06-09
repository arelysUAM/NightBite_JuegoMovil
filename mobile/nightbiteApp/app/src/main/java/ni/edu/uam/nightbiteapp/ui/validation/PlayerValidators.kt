package ni.edu.uam.nightbiteapp.ui.validation

object PlayerValidators {

    const val NICKNAME_MAX_LENGTH = 30
    const val DRIVER_NAME_MAX_LENGTH = 80
    const val OPTION_MAX_LENGTH = 30

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

    fun validateNickname(nickname: String): String? {
        if (nickname.isBlank()) {
            return "El apodo del repartidor es obligatorio."
        }

        val normalizedNickname = nickname.trim()

        if (normalizedNickname.contains(" ")) {
            return "El apodo no debe contener espacios."
        }

        if (normalizedNickname != normalizedNickname.lowercase()) {
            return "El apodo debe estar en minúsculas."
        }

        if (normalizedNickname.length > NICKNAME_MAX_LENGTH) {
            return "El apodo no debe superar los 30 caracteres."
        }

        if (!normalizedNickname.matches(Regex("^[a-z0-9_]+$"))) {
            return "Formato permitido: letras, números y guiones bajos."
        }

        return null
    }

    fun validateDriverName(driverName: String): String? {
        if (driverName.isBlank()) {
            return "El nombre del repartidor es obligatorio."
        }

        val normalizedDriverName = driverName.trim()

        if (normalizedDriverName.length > DRIVER_NAME_MAX_LENGTH) {
            return "El nombre del repartidor no debe superar los 80 caracteres."
        }

        if (normalizedDriverName.contains(" ")) {
            return "El nombre del repartidor no debe contener espacios."
        }

        if (!normalizedDriverName.matches(Regex("^[A-Za-zÁÉÍÓÚáéíóúÑñ]+$"))) {
            return "El nombre del repartidor solo puede contener letras."
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
        return value
            .replace(Regex("\\s+"), " ")
            .trimStart()
            .split(" ")
            .joinToString(" ") { word ->
                word.lowercase().replaceFirstChar { char ->
                    if (char.isLowerCase()) char.titlecase() else char.toString()
                }
            }
    }
}