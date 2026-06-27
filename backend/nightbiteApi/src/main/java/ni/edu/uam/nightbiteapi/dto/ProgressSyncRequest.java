package ni.edu.uam.nightbiteapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProgressSyncRequest {

    @NotNull(message = "El nivel máximo desbloqueado es obligatorio")
    @Min(value = 0, message = "El nivel máximo desbloqueado no puede ser menor a 0")
    @Max(value = 4, message = "El nivel máximo desbloqueado no puede ser mayor a 4")
    private Integer maxUnlockedLevel = 0;

    @Valid
    private List<LevelResultSyncRequest> levelResults = new ArrayList<>();

    @Valid
    private List<BadgeSyncRequest> badges = new ArrayList<>();

    public ProgressSyncRequest() {
    }
}