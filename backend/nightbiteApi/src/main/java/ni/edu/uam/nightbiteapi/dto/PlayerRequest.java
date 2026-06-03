package ni.edu.uam.nightbiteapi.dto;

import ni.edu.uam.nightbiteapi.enums.Gender;

/**
 * DTO utilizado para recibir los datos de la ficha/personaje del repartidor.
 */
public class PlayerRequest {

    private Long userAccountId;
    private String nickname;
    private String driverName;
    private Gender gender;
    private String helmetColor;
    private String motorcycleType;

    public PlayerRequest() {
    }

    public PlayerRequest(Long userAccountId, String nickname, String driverName,
                         Gender gender, String helmetColor, String motorcycleType) {
        this.userAccountId = userAccountId;
        this.nickname = nickname;
        this.driverName = driverName;
        this.gender = gender;
        this.helmetColor = helmetColor;
        this.motorcycleType = motorcycleType;
    }

    public Long getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(Long userAccountId) {
        this.userAccountId = userAccountId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getHelmetColor() {
        return helmetColor;
    }

    public void setHelmetColor(String helmetColor) {
        this.helmetColor = helmetColor;
    }

    public String getMotorcycleType() {
        return motorcycleType;
    }

    public void setMotorcycleType(String motorcycleType) {
        this.motorcycleType = motorcycleType;
    }
}