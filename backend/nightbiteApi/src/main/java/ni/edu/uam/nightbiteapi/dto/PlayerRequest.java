package ni.edu.uam.nightbiteapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import ni.edu.uam.nightbiteapi.enums.Gender;

/**
 * DTO utilizado para recibir los datos de la ficha/personaje del repartidor.
 */
public class PlayerRequest {

    @NotNull(message = "El id de la cuenta de usuario es obligatorio")
    private Long userAccountId;

    @NotBlank(message = "El apodo del repartidor es obligatorio")
    @Size(max = 30, message = "El apodo no debe superar los 30 caracteres")
    @Pattern(
            regexp = "^[a-z0-9_]+$",
            message = "El apodo solo puede contener letras minúsculas, números y guion bajo"
    )
    private String nickname;

    @NotBlank(message = "El nombre del repartidor es obligatorio")
    @Size(max = 80, message = "El nombre del repartidor no debe superar los 80 caracteres")
    @Pattern(
            regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ]+$",
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

    public PlayerRequest(
            Long userAccountId,
            String nickname,
            String driverName,
            Gender gender,
            String helmetColor,
            String motorcycleType
    ) {
        this.userAccountId = userAccountId;
        this.nickname = nickname;
        this.driverName = driverName;
        this.gender = gender;
        this.helmetColor = helmetColor;
        this.motorcycleType = motorcycleType;
    }

    public Long getUserAccountId() {
        return userAccountId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getDriverName() {
        return driverName;
    }

    public Gender getGender() {
        return gender;
    }

    public String getHelmetColor() {
        return helmetColor;
    }

    public String getMotorcycleType() {
        return motorcycleType;
    }

    public void setUserAccountId(Long userAccountId) {
        this.userAccountId = userAccountId;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setHelmetColor(String helmetColor) {
        this.helmetColor = helmetColor;
    }

    public void setMotorcycleType(String motorcycleType) {
        this.motorcycleType = motorcycleType;
    }
}