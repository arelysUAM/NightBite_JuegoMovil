package ni.edu.uam.nightbiteapi.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private Integer age;
    private LocalDateTime createdAt;
    private PlayerSummaryResponse player;

    public UserResponse(
            Long id,
            String username,
            String email,
            Integer age,
            LocalDateTime createdAt,
            PlayerSummaryResponse player
    ) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.age = age;
        this.createdAt = createdAt;
        this.player = player;
    }
}