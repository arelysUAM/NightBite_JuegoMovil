package ni.edu.uam.nightbiteapi.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadgeSyncRequest {

    @NotNull(message = "El nivel de la insignia es obligatorio")
    @Min(value = 0, message = "El nivel de la insignia no puede ser menor a 0")
    @Max(value = 4, message = "El nivel de la insignia no puede ser mayor a 4")
    private Integer levelId;

    @NotBlank(message = "El código de insignia es obligatorio")
    @Size(max = 80, message = "El código de insignia no debe superar los 80 caracteres")
    private String badgeCode;

    public BadgeSyncRequest() {
    }
}