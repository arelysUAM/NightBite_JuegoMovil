package ni.edu.uam.nightbiteapi.controllers;

import ni.edu.uam.nightbiteapi.dto.MessageResponse;
import ni.edu.uam.nightbiteapi.dto.PlayerRequest;
import ni.edu.uam.nightbiteapi.dto.PlayerResponse;
import ni.edu.uam.nightbiteapi.services.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST encargado de exponer los endpoints relacionados con
 * la ficha/personaje del repartidor.
 */
@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(
            PlayerService playerService
    ) {
        this.playerService = playerService;
    }

    @GetMapping
    public ResponseEntity<List<PlayerResponse>> getAllPlayers() {
        List<PlayerResponse> players = playerService.getAllPlayers();
        return ResponseEntity.ok(players);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPlayerById(
            @PathVariable Long id
    ) {
        return playerService.getPlayerById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new MessageResponse("Ficha de repartidor no encontrada")));
    }

    @GetMapping("/account/{userAccountId}")
    public ResponseEntity<?> getPlayerByUserAccountId(
            @PathVariable Long userAccountId
    ) {
        return playerService.getPlayerByUserAccountId(userAccountId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new MessageResponse("Ficha de repartidor no encontrada para esta cuenta")));
    }

    @PostMapping
    public ResponseEntity<PlayerResponse> savePlayer(
            @RequestBody PlayerRequest request
    ) {
        PlayerResponse savedPlayer = playerService.savePlayer(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedPlayer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePlayer(
            @PathVariable Long id,
            @RequestBody PlayerRequest request
    ) {
        return playerService.updatePlayer(id, request)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new MessageResponse("Ficha de repartidor no encontrada")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deletePlayer(
            @PathVariable Long id
    ) {
        boolean deleted = playerService.deletePlayer(id);

        if (!deleted) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Ficha de repartidor no encontrada"));
        }

        return ResponseEntity.ok(
                new MessageResponse("Ficha de repartidor eliminada correctamente")
        );
    }
}