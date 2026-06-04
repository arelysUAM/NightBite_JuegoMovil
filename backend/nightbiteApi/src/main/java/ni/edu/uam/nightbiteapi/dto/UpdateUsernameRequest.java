package ni.edu.uam.nightbiteapi.dto;

/**
 * DTO utilizado para solicitar el cambio de nombre de usuario.
 */
public class UpdateUsernameRequest {

    private String newUsername;

    public UpdateUsernameRequest() {
    }

    public UpdateUsernameRequest(String newUsername) {
        this.newUsername = newUsername;
    }

    public String getNewUsername() {
        return newUsername;
    }

    public void setNewUsername(String newUsername) {
        this.newUsername = newUsername;
    }
}