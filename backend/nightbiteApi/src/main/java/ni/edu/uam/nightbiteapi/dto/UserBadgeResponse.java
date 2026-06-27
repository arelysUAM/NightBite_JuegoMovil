package ni.edu.uam.nightbiteapi.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserBadgeResponse {

    private Integer levelId;
    private String badgeCode;
    private LocalDateTime unlockedAt;

    public UserBadgeResponse(
            Integer levelId,
            String badgeCode,
            LocalDateTime unlockedAt
    ) {
        this.levelId = levelId;
        this.badgeCode = badgeCode;
        this.unlockedAt = unlockedAt;
    }
}