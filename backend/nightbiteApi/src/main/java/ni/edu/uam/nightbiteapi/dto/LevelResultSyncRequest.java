package ni.edu.uam.nightbiteapi.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LevelResultSyncRequest {

    @NotNull(message = "El nivel es obligatorio")
    @Min(value = 0, message = "El nivel no puede ser menor a 0")
    @Max(value = 4, message = "El nivel no puede ser mayor a 4")
    private Integer levelId;

    @NotNull(message = "Las estrellas son obligatorias")
    @Min(value = 0, message = "Las estrellas no pueden ser menores a 0")
    @Max(value = 3, message = "Las estrellas no pueden ser mayores a 3")
    private Integer bestStars;

    @NotNull(message = "El puntaje es obligatorio")
    @Min(value = 0, message = "El puntaje no puede ser menor a 0")
    private Integer bestScore;

    @NotNull(message = "Los pedidos completados son obligatorios")
    @Min(value = 0, message = "Los pedidos completados no pueden ser menores a 0")
    private Integer completedOrders;

    @NotNull(message = "El total de pedidos es obligatorio")
    @Min(value = 0, message = "El total de pedidos no puede ser menor a 0")
    private Integer totalOrders;

    @NotBlank(message = "El tipo de resultado es obligatorio")
    @Size(max = 60, message = "El tipo de resultado no debe superar los 60 caracteres")
    private String resultType;

    @NotNull(message = "El tiempo total es obligatorio")
    @DecimalMin(value = "0.0", message = "El tiempo total no puede ser negativo")
    private Float elapsedTimeSeconds;

    @NotNull(message = "El promedio de entrega es obligatorio")
    @DecimalMin(value = "0.0", message = "El promedio de entrega no puede ser negativo")
    private Float averageDeliveryTimeSeconds;

    public LevelResultSyncRequest() {
    }
}