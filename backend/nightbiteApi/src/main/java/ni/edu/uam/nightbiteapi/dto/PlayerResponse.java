package ni.edu.uam.nightbiteapi.dto;

import java.time.LocalDateTime;

/**
 * DTO utilizado para devolver los datos de un jugador al cliente.
 *
 * Este objeto evita exponer directamente la entidad Player y permite controlar
 * qué información será enviada como respuesta desde la API.
 */
public class PlayerResponse {

    private Long id;
    private String username;
    private String email;
    private LocalDateTime createdAt;

    public PlayerResponse() {
    }

    public PlayerResponse(Long id, String username, String email, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.createdAt = createdAt;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}