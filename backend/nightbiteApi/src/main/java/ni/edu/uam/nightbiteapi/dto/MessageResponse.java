package ni.edu.uam.nightbiteapi.dto;

/**
 * DTO utilizado para enviar mensajes simples desde la API.
 *
 * Se usa principalmente para respuestas de error o confirmaciones.
 */
public class MessageResponse {

    private String message;

    public MessageResponse() {
    }

    public MessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}