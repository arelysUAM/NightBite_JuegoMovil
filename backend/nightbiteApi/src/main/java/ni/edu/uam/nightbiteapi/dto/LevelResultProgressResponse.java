package ni.edu.uam.nightbiteapi.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LevelResultProgressResponse {

    private Integer levelId;
    private Integer bestStars;
    private Integer bestScore;
    private Integer completedOrders;
    private Integer totalOrders;
    private String resultType;
    private Float elapsedTimeSeconds;
    private Float averageDeliveryTimeSeconds;
    private LocalDateTime updatedAt;

    public LevelResultProgressResponse(
            Integer levelId,
            Integer bestStars,
            Integer bestScore,
            Integer completedOrders,
            Integer totalOrders,
            String resultType,
            Float elapsedTimeSeconds,
            Float averageDeliveryTimeSeconds,
            LocalDateTime updatedAt
    ) {
        this.levelId = levelId;
        this.bestStars = bestStars;
        this.bestScore = bestScore;
        this.completedOrders = completedOrders;
        this.totalOrders = totalOrders;
        this.resultType = resultType;
        this.elapsedTimeSeconds = elapsedTimeSeconds;
        this.averageDeliveryTimeSeconds = averageDeliveryTimeSeconds;
        this.updatedAt = updatedAt;
    }
}