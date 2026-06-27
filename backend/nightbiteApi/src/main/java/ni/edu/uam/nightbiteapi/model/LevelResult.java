package ni.edu.uam.nightbiteapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "level_results",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_level_result_user_level",
                        columnNames = {"user_account_id", "level_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class LevelResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_level_result")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_account_id", nullable = false)
    private UserAccount userAccount;

    @Column(name = "level_id", nullable = false)
    private Integer levelId;

    @Column(name = "best_stars", nullable = false)
    private Integer bestStars = 0;

    @Column(name = "best_score", nullable = false)
    private Integer bestScore = 0;

    @Column(name = "completed_orders", nullable = false)
    private Integer completedOrders = 0;

    @Column(name = "total_orders", nullable = false)
    private Integer totalOrders = 0;

    @Column(name = "result_type", nullable = false, length = 60)
    private String resultType;

    @Column(name = "elapsed_time_seconds", nullable = false)
    private Float elapsedTimeSeconds = 0f;

    @Column(name = "average_delivery_time_seconds", nullable = false)
    private Float averageDeliveryTimeSeconds = 0f;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.updatedAt = LocalDateTime.now();

        if (this.bestStars == null) {
            this.bestStars = 0;
        }

        if (this.bestScore == null) {
            this.bestScore = 0;
        }

        if (this.completedOrders == null) {
            this.completedOrders = 0;
        }

        if (this.totalOrders == null) {
            this.totalOrders = 0;
        }

        if (this.elapsedTimeSeconds == null) {
            this.elapsedTimeSeconds = 0f;
        }

        if (this.averageDeliveryTimeSeconds == null) {
            this.averageDeliveryTimeSeconds = 0f;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}