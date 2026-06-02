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
 * Controlador REST encargado de exponer los endpoints relacionados con jugadores.
 *
 * Esta clase recibe las peticiones HTTP enviadas desde Postman o desde la app Android
 * y delega la lógica al servicio PlayerService.
 */
@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final PlayerService playerService;

    /**
     * Constructor que inyecta el servicio de jugadores.
     *
     * @param playerService servicio que contiene la lógica de jugadores.
     */
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    /**
     * Endpoint para obtener todos los jugadores registrados.
     *
     * Método HTTP: GET
     * URL: /api/players
     *
     * @return lista de jugadores registrados.
     */
    @GetMapping
    public ResponseEntity<List<PlayerResponse>> getAllPlayers() {
        List<PlayerResponse> players = playerService.getAllPlayers();
        return ResponseEntity.ok(players);
    }

    /**
     * Endpoint para buscar un jugador por su identificador.
     *
     * Método HTTP: GET
     * URL: /api/players/{id}
     *
     * Si el jugador existe, devuelve sus datos.
     * Si no existe, devuelve un mensaje con código 404.
     *
     * @param id identificador del jugador.
     * @return jugador encontrado o mensaje de error.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPlayerById(@PathVariable Long id) {
        return playerService.getPlayerById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new MessageResponse("Jugador no encontrado")));
    }

    /**
     * Endpoint para registrar un nuevo jugador.
     *
     * Método HTTP: POST
     * URL: /api/players
     *
     * @param request datos del jugador recibidos en formato JSON.
     * @return jugador registrado.
     */
    @PostMapping
    public ResponseEntity<PlayerResponse> savePlayer(@RequestBody PlayerRequest request) {
        PlayerResponse savedPlayer = playerService.savePlayer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPlayer);
    }

    /**
     * Endpoint para actualizar un jugador existente.
     *
     * Método HTTP: PUT
     * URL: /api/players/{id}
     *
     * Si el jugador existe, actualiza sus datos.
     * Si no existe, devuelve un mensaje con código 404.
     *
     * @param id identificador del jugador a actualizar.
     * @param request nuevos datos del jugador.
     * @return jugador actualizado o mensaje de error.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePlayer(
            @PathVariable Long id,
            @RequestBody PlayerRequest request
    ) {
        return playerService.updatePlayer(id, request)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new MessageResponse("Jugador no encontrado")));
    }

    /**
     * Endpoint para eliminar un jugador por su identificador.
     *
     * Método HTTP: DELETE
     * URL: /api/players/{id}
     *
     * Si el jugador existe, lo elimina correctamente.
     * Si no existe, devuelve un mensaje con código 404.
     *
     * @param id identificador del jugador a eliminar.
     * @return mensaje de confirmación o mensaje de error.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deletePlayer(@PathVariable Long id) {
        boolean deleted = playerService.deletePlayer(id);

        if (!deleted) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Jugador no encontrado"));
        }

        return ResponseEntity.ok(new MessageResponse("Jugador eliminado correctamente"));
    }
}