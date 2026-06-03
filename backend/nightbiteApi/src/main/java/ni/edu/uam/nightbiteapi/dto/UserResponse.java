package ni.edu.uam.nightbiteapi.dto;

import java.time.LocalDateTime;

public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private Integer age;
    private LocalDateTime createdAt;
    private PlayerSummaryResponse player;

    public UserResponse() {
    }

    public UserResponse(Long id, String username, String email, Integer age, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.age = age;
        this.createdAt = createdAt;
    }

    public UserResponse(Long id, String username, String email, Integer age,
                        LocalDateTime createdAt, PlayerSummaryResponse player) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.age = age;
        this.createdAt = createdAt;
        this.player = player;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public PlayerSummaryResponse getPlayer() {
        return player;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setPlayer(PlayerSummaryResponse player) {
        this.player = player;
    }
}