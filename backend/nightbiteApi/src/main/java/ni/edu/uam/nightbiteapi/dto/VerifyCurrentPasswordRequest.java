package ni.edu.uam.nightbiteapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO utilizado para verificar si la contraseña actual ingresada
 * coincide con la contraseña guardada de la cuenta.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerifyCurrentPasswordRequest {

    @NotBlank(message = "La contraseña actual es obligatoria")
    private String currentPassword;
}