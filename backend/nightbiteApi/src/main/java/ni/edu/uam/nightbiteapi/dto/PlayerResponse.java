package ni.edu.uam.nightbiteapi.dto;

import ni.edu.uam.nightbiteapi.enums.Gender;

import java.time.LocalDateTime;

/**
 * DTO utilizado para devolver los datos de la ficha/personaje del repartidor.
 */
public class PlayerResponse {

    private Long id;
    private Long userAccountId;
    private String nickname;
    private String driverName;
    private Gender gender;
    private String helmetColor;
    private String motorcycleType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PlayerResponse() {
    }

    public PlayerResponse(Long id, Long userAccountId, String nickname, String driverName,
                          Gender gender, String helmetColor, String motorcycleType,
                          LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userAccountId = userAccountId;
        this.nickname = nickname;
        this.driverName = driverName;
        this.gender = gender;
        this.helmetColor = helmetColor;
        this.motorcycleType = motorcycleType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getUserAccountId() {
        return userAccountId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getDriverName() {
        return driverName;
    }

    public Gender getGender() {
        return gender;
    }

    public String getHelmetColor() {
        return helmetColor;
    }

    public String getMotorcycleType() {
        return motorcycleType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}