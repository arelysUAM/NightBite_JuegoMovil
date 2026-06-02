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

    /**
     * Actualiza los datos básicos de un jugador existente.
     *
     * Este método primero busca el jugador en la base de datos para conservar
     * valores que no deben perderse, como la fecha de creación.
     *
     * @param id identificador del jugador a actualizar.
     * @param playerData nuevos datos recibidos desde la petición.
     * @return jugador actualizado o null si no existe.
     */
    public Player updatePlayer(Long id, Player playerData) {
        Player existingPlayer = playerRepository.findById(id).orElse(null);

        if (existingPlayer == null) {
            return null;
        }

        existingPlayer.setUsername(playerData.getUsername());
        existingPlayer.setEmail(playerData.getEmail());

        return playerRepository.save(existingPlayer);
    }

    public void deletePlayer(Long id) {
        playerRepository.deleteById(id);
    }
}