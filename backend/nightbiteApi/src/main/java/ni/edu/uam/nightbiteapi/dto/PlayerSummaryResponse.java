package ni.edu.uam.nightbiteapi.dto;

import ni.edu.uam.nightbiteapi.enums.Gender;

/**
 * DTO resumido para mostrar el Player asignado dentro de la respuesta de UserAccount.
 *
 * No incluye userAccountId para evitar repetir información innecesaria.
 */
public class PlayerSummaryResponse {

    private Long id;
    private String nickname;
    private String driverName;
    private Gender gender;
    private String helmetColor;
    private String motorcycleType;

    public PlayerSummaryResponse() {
    }

    public PlayerSummaryResponse(Long id, String nickname, String driverName,
                                 Gender gender, String helmetColor, String motorcycleType) {
        this.id = id;
        this.nickname = nickname;
        this.driverName = driverName;
        this.gender = gender;
        this.helmetColor = helmetColor;
        this.motorcycleType = motorcycleType;
    }

    public Long getId() {
        return id;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setHelmetColor(String helmetColor) {
        this.helmetColor = helmetColor;
    }

    public void setMotorcycleType(String motorcycleType) {
        this.motorcycleType = motorcycleType;
    }
}