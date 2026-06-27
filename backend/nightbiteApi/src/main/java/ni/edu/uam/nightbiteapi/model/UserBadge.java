package ni.edu.uam.nightbiteapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_badges",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_badge_user_level",
                        columnNames = {"user_account_id", "level_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class UserBadge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_badge")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_account_id", nullable = false)
    private UserAccount userAccount;

    @Column(name = "level_id", nullable = false)
    private Integer levelId;

    @Column(name = "badge_code", nullable = false, length = 80)
    private String badgeCode;

    @Column(name = "unlocked_at", nullable = false, updatable = false)
    private LocalDateTime unlockedAt;

    @PrePersist
    public void prePersist() {
        this.unlockedAt = LocalDateTime.now();
    }
}