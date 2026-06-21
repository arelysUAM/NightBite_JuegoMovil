package ni.edu.uam.nightbiteapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO utilizado para solicitar el cambio de nombre de usuario.
 */

@Setter
@Getter
public class UpdateUsernameRequest {

    @NotBlank(message = "El nuevo nombre de usuario es obligatorio")
    @Size(min = 4, max = 16, message = "El nuevo nombre de usuario debe tener entre 4 y 16 caracteres")
    @Pattern(
            regexp = "^[a-z0-9_]+$",
            message = "El nuevo nombre de usuario solo puede contener letras minúsculas, números y guion bajo"
    )
    private String newUsername;
}