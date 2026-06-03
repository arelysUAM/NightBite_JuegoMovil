package ni.edu.uam.nightbiteapp.data.remote.dto

/**
 * DTO utilizado para leer mensajes simples enviados por la API.
 *
 * Se usa principalmente para errores de validación y respuestas
 * informativas del backend.
 */
data class MessageResponse(
    val message: String
)