package ni.edu.uam.nightbiteapi.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ProgressResponse {

    private Long userAccountId;
    private Integer maxUnlockedLevel;
    private LocalDateTime updatedAt;
    private List<LevelResultProgressResponse> levelResults;
    private List<UserBadgeResponse> badges;

    public ProgressResponse(
            Long userAccountId,
            Integer maxUnlockedLevel,
            LocalDateTime updatedAt,
            List<LevelResultProgressResponse> levelResults,
            List<UserBadgeResponse> badges
    ) {
        this.userAccountId = userAccountId;
        this.maxUnlockedLevel = maxUnlockedLevel;
        this.updatedAt = updatedAt;
        this.levelResults = levelResults;
        this.badges = badges;
    }
}