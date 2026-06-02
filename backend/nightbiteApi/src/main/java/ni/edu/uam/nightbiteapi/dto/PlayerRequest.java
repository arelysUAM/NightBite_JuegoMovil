package ni.edu.uam.nightbiteapi.dto;

/**
 * DTO utilizado para recibir los datos de un jugador desde una petición HTTP.
 *
 * Este objeto representa la información que el cliente, como Postman o Android,
 * envía al backend para crear o actualizar un jugador.
 */
public class PlayerRequest {

    private String username;
    private String email;

    public PlayerRequest() {
    }

    public PlayerRequest(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}