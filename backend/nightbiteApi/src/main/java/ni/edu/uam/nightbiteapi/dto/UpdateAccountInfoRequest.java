package ni.edu.uam.nightbiteapi.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ni.edu.uam.nightbiteapi.enums.Gender;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountInfoRequest {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 4, message = "El nombre de usuario debe tener al menos 4 caracteres")
    @Size(max = 16, message = "El nombre de usuario no debe superar los 16 caracteres")
    @Pattern(
            regexp = "^[a-z0-9_]+$",
            message = "El nombre de usuario solo puede contener letras minúsculas, números y guion bajo"
    )
    private String username;

    @NotNull(message = "La edad es obligatoria")
    @Min(value = 13, message = "Debes tener 13 años o más")
    @Max(value = 120, message = "La edad no puede ser mayor a 120 años")
    private Integer age;

    @NotNull(message = "El género es obligatorio")
    private Gender gender;
}