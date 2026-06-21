package ni.edu.uam.nightbiteapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import ni.edu.uam.nightbiteapi.enums.Gender;

@Setter
@Getter
public class PlayerRequest {

    @NotNull(message = "El id de la cuenta de usuario es obligatorio")
    private Long userAccountId;

    @NotBlank(message = "El nombre del repartidor es obligatorio")
    @Size(max = 80, message = "El nombre del repartidor no debe superar los 80 caracteres")
    @Pattern(
            regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ]+( [A-Za-zÁÉÍÓÚáéíóúÑñ]+)?$",
            message = "El nombre del repartidor solo puede contener letras"
    )
    private String driverName;

    @NotNull(message = "El género es obligatorio")
    private Gender gender;

    @NotBlank(message = "El color del casco es obligatorio")
    @Size(max = 30, message = "El color del casco no debe superar los 30 caracteres")
    private String helmetColor;

    @NotBlank(message = "El tipo de moto es obligatorio")
    @Size(max = 30, message = "El tipo de moto no debe superar los 30 caracteres")
    private String motorcycleType;

    public PlayerRequest() {
    }

}