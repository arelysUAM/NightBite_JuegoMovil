package ni.edu.uam.nightbiteapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRegisterRequest {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 4, max = 16, message = "El nombre de usuario debe tener entre 4 y 16 caracteres")
    @Pattern(
            regexp = "^[a-z0-9_]+$",
            message = "El nombre de usuario solo puede contener letras minúsculas, números y guion bajo"
    )
    private String username;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Ingresa un correo electrónico válido")
    @Size(max = 100, message = "El correo no debe superar los 100 caracteres")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 20, message = "La contraseña debe tener entre 8 y 20 caracteres")
    private String password;

    @NotNull(message = "La edad es obligatoria")
    @Min(value = 13, message = "Debes tener 13 años o más para crear una cuenta")
    private Integer age;

    public UserRegisterRequest(
            String username,
            String email,
            String password,
            Integer age
    ) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.age = age;
    }

}