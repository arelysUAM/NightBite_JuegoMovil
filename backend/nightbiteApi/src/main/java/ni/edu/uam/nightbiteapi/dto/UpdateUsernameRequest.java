package ni.edu.uam.nightbiteapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para solicitar el cambio de nombre de usuario.
 */
public class UpdateUsernameRequest {

    @NotBlank(message = "El nuevo nombre de usuario es obligatorio")
    @Size(min = 4, max = 16, message = "El nuevo nombre de usuario debe tener entre 4 y 16 caracteres")
    @Pattern(
            regexp = "^[a-z0-9_]+$",
            message = "El nuevo nombre de usuario solo puede contener letras minúsculas, números y guion bajo"
    )
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