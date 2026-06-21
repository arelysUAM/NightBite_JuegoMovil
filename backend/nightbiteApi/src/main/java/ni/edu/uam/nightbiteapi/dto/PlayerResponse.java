package ni.edu.uam.nightbiteapi.dto;

import lombok.Getter;
import lombok.Setter;
import ni.edu.uam.nightbiteapi.enums.Gender;

import java.time.LocalDateTime;

@Setter
@Getter
public class PlayerResponse {

    private Long id;
    private Long userAccountId;
    private String driverName;
    private Gender gender;
    private String helmetColor;
    private String motorcycleType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PlayerResponse(
            Long id,
            Long userAccountId,
            String driverName,
            Gender gender,
            String helmetColor,
            String motorcycleType,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.userAccountId = userAccountId;
        this.driverName = driverName;
        this.gender = gender;
        this.helmetColor = helmetColor;
        this.motorcycleType = motorcycleType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}