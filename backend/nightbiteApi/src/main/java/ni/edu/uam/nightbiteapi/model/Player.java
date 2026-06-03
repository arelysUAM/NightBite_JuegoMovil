package ni.edu.uam.nightbiteapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ni.edu.uam.nightbiteapi.enums.Gender;

import java.time.LocalDateTime;

/**
 * Entidad que representa la ficha/personaje del repartidor dentro del juego NightBite.
 *
 * Player no representa la cuenta real de inicio de sesión.
 * La cuenta real pertenece a UserAccount.
 */
@Entity
@Table(name = "players")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_player")
    private Long id;

    /**
     * Cuenta real asociada a la ficha del repartidor.
     * Una cuenta debe tener como máximo una ficha Player.
     */
    @OneToOne
    @JoinColumn(name = "user_account_id", nullable = false, unique = true)
    private UserAccount userAccount;

    /**
     * Apodo visible dentro del juego.
     */
    @Column(name = "nickname", nullable = false, unique = true, length = 30)
    private String nickname;

    /**
     * Nombre del repartidor dentro de la ficha de contratación nocturna.
     */
    @Column(name = "driver_name", nullable = false, length = 80)
    private String driverName;

    /**
     * Género del personaje/repartidor.
     * Se almacena como texto: FEMENINO o MASCULINO.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, length = 20)
    private Gender gender;

    /**
     * Color del casco seleccionado.
     */
    @Column(name = "helmet_color", nullable = false, length = 30)
    private String helmetColor;

    /**
     * Tipo de moto seleccionado.
     */
    @Column(name = "motorcycle_type", nullable = false, length = 30)
    private String motorcycleType;

    /**
     * Fecha de creación de la ficha.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Fecha de última actualización de la ficha.
     */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}