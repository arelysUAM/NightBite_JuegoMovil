package ni.edu.uam.nightbiteapi.controllers;

import ni.edu.uam.nightbiteapi.dto.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador simple para verificar si la API está disponible.
 */
@RestController
public class HealthController {

    @GetMapping("/api/health")
    public ResponseEntity<MessageResponse> checkHealth() {
        return ResponseEntity.ok(
                new MessageResponse("Servidor disponible")
        );
    }
}