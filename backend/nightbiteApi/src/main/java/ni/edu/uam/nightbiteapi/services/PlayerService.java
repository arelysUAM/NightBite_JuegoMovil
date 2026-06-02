package ni.edu.uam.nightbiteapi.services;

import ni.edu.uam.nightbiteapi.dto.PlayerRequest;
import ni.edu.uam.nightbiteapi.dto.PlayerResponse;
import ni.edu.uam.nightbiteapi.model.Player;
import ni.edu.uam.nightbiteapi.repositories.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio encargado de manejar la lógica relacionada con los jugadores.
 *
 * Esta capa actúa como intermediaria entre el controlador y el repositorio.
 * Permite mantener la lógica del backend separada de los endpoints REST.
 */
@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    /**
     * Constructor que inyecta el repositorio de jugadores.
     *
     * @param playerRepository repositorio utilizado para acceder a PostgreSQL.
     */
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    /**
     * Convierte una entidad Player en un DTO PlayerResponse.
     *
     * @param player entidad obtenida desde la base de datos.
     * @return DTO con los datos que serán enviados al cliente.
     */
    private PlayerResponse mapToResponse(Player player) {
        return new PlayerResponse(
                player.getId(),
                player.getUsername(),
                player.getEmail(),
                player.getCreatedAt()
        );
    }

    /**
     * Obtiene la lista completa de jugadores registrados.
     *
     * @return lista de jugadores convertidos a DTO de respuesta.
     */
    public List<PlayerResponse> getAllPlayers() {
        return playerRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Busca un jugador por su identificador.
     *
     * @param id identificador del jugador.
     * @return Optional con el jugador encontrado o vacío si no existe.
     */
    public Optional<PlayerResponse> getPlayerById(Long id) {
        return playerRepository.findById(id)
                .map(this::mapToResponse);
    }

    /**
     * Guarda un nuevo jugador en la base de datos.
     *
     * @param request datos recibidos desde la petición HTTP.
     * @return jugador guardado convertido a DTO de respuesta.
     */
    public PlayerResponse savePlayer(PlayerRequest request) {
        Player player = new Player();
        player.setUsername(request.getUsername());
        player.setEmail(request.getEmail());

        Player savedPlayer = playerRepository.save(player);

        return mapToResponse(savedPlayer);
    }

    /**
     * Actualiza los datos básicos de un jugador existente.
     *
     * Este método busca primero el jugador para conservar datos internos como
     * la fecha de creación.
     *
     * @param id identificador del jugador a actualizar.
     * @param request nuevos datos recibidos desde la petición HTTP.
     * @return Optional con el jugador actualizado o vacío si no existe.
     */
    public Optional<PlayerResponse> updatePlayer(Long id, PlayerRequest request) {
        Optional<Player> optionalPlayer = playerRepository.findById(id);

        if (optionalPlayer.isEmpty()) {
            return Optional.empty();
        }

        Player existingPlayer = optionalPlayer.get();
        existingPlayer.setUsername(request.getUsername());
        existingPlayer.setEmail(request.getEmail());

        Player updatedPlayer = playerRepository.save(existingPlayer);

        return Optional.of(mapToResponse(updatedPlayer));
    }

    /**
     * Elimina un jugador usando su identificador.
     *
     * @param id identificador del jugador a eliminar.
     * @return true si el jugador existía y fue eliminado; false si no existe.
     */
    public boolean deletePlayer(Long id) {
        if (!playerRepository.existsById(id)) {
            return false;
        }

        playerRepository.deleteById(id);
        return true;
    }
}