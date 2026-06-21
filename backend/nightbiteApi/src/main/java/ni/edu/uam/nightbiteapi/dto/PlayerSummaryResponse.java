package ni.edu.uam.nightbiteapi.dto;

import lombok.Getter;
import lombok.Setter;
import ni.edu.uam.nightbiteapi.enums.Gender;

@Setter
@Getter
public class PlayerSummaryResponse {

    private Long id;
    private String driverName;
    private Gender gender;
    private String helmetColor;
    private String motorcycleType;

    public PlayerSummaryResponse(
            Long id,
            String driverName,
            Gender gender,
            String helmetColor,
            String motorcycleType
    ) {
        this.id = id;
        this.driverName = driverName;
        this.gender = gender;
        this.helmetColor = helmetColor;
        this.motorcycleType = motorcycleType;
    }

}