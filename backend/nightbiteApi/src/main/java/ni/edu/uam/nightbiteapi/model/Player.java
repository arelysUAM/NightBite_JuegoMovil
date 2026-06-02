package ni.edu.uam.nightbiteapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entidad que representa a un jugador dentro del sistema NightBite.
 *
 * Esta clase se mapea con la tabla "players" en PostgreSQL mediante JPA.
 * Cada registro almacena la información básica del jugador que utilizará
 * la aplicación móvil.
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
     * Nombre de usuario del jugador.
     * Debe ser único para evitar duplicados dentro del sistema.
     */
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    /**
     * Fecha y hora en que el jugador fue registrado.
     * Este valor se asigna automáticamente antes de guardar el registro.
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * Método ejecutado automáticamente antes de insertar el registro en la base de datos.
     * Sirve para asignar la fecha de creación del jugador.
     */
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}