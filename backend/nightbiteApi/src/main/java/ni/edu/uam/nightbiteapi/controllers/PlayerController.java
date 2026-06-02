package ni.edu.uam.nightbiteapi.controllers;

import ni.edu.uam.nightbiteapi.model.Player;
import ni.edu.uam.nightbiteapi.services.PlayerService;
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

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    public List<Player> getAllPlayers() {
        return playerService.getAllPlayers();
    }

    @GetMapping("/{id}")
    public Player getPlayerById(@PathVariable Long id) {
        return playerService.getPlayerById(id);
    }

    @PostMapping
    public Player savePlayer(@RequestBody Player player) {
        return playerService.savePlayer(player);
    }

    /**
     * Endpoint para actualizar un jugador existente.
     *
     * Método HTTP: PUT
     * URL: /api/players/{id}
     *
     * @param id identificador del jugador a actualizar.
     * @param player nuevos datos del jugador.
     * @return jugador actualizado.
     */
    @PutMapping("/{id}")
    public Player updatePlayer(@PathVariable Long id, @RequestBody Player player) {
        return playerService.updatePlayer(id, player);
    }

    @DeleteMapping("/{id}")
    public void deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
    }
}