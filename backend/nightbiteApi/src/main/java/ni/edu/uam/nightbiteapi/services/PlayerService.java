package ni.edu.uam.nightbiteapi.services;

import ni.edu.uam.nightbiteapi.model.Player;
import ni.edu.uam.nightbiteapi.repositories.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio encargado de manejar la lógica relacionada con los jugadores.
 *
 * Esta capa actúa como intermediaria entre el controlador y el repositorio.
 * Permite mantener la lógica del backend separada de los endpoints REST.
 */
@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public Player getPlayerById(Long id) {
        return playerRepository.findById(id).orElse(null);
    }

    public Player savePlayer(Player player) {
        return playerRepository.save(player);
    }

    public void deletePlayer(Long id) {
        playerRepository.deleteById(id);
    }
}