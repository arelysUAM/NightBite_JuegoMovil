package ni.edu.uam.nightbiteapi.services;

import ni.edu.uam.nightbiteapi.dto.PlayerRequest;
import ni.edu.uam.nightbiteapi.dto.PlayerResponse;
import ni.edu.uam.nightbiteapi.model.Player;
import ni.edu.uam.nightbiteapi.model.UserAccount;
import ni.edu.uam.nightbiteapi.repositories.PlayerRepository;
import ni.edu.uam.nightbiteapi.repositories.UserAccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Servicio encargado de manejar la lógica relacionada con la ficha/personaje
 * del repartidor dentro del juego.
 */
@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final UserAccountRepository userAccountRepository;

    private static final Set<String> ALLOWED_HELMET_COLORS = Set.of(
            "Negro",
            "Rojo",
            "Azul",
            "Blanco",
            "Amarillo"
    );

    private static final Set<String> ALLOWED_MOTORCYCLE_TYPES = Set.of(
            "Estándar",
            "Scooter",
            "Deportiva",
            "Retro",
            "Delivery"
    );

    public PlayerService(PlayerRepository playerRepository,
                         UserAccountRepository userAccountRepository) {
        this.playerRepository = playerRepository;
        this.userAccountRepository = userAccountRepository;
    }

    private PlayerResponse mapToResponse(Player player) {
        return new PlayerResponse(
                player.getId(),
                player.getUserAccount().getId(),
                player.getNickname(),
                player.getDriverName(),
                player.getGender(),
                player.getHelmetColor(),
                player.getMotorcycleType(),
                player.getCreatedAt(),
                player.getUpdatedAt()
        );
    }

    private void validateRequiredFields(PlayerRequest request) {
        if (request.getUserAccountId() == null) {
            throw new IllegalArgumentException("El id de la cuenta de usuario es obligatorio");
        }

        if (request.getNickname() == null || request.getNickname().trim().isEmpty()) {
            throw new IllegalArgumentException("El apodo del repartidor es obligatorio");
        }

        if (request.getDriverName() == null || request.getDriverName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del repartidor es obligatorio");
        }

        if (request.getGender() == null) {
            throw new IllegalArgumentException("El género es obligatorio");
        }

        if (request.getHelmetColor() == null || request.getHelmetColor().trim().isEmpty()) {
            throw new IllegalArgumentException("El color del casco es obligatorio");
        }

        if (request.getMotorcycleType() == null || request.getMotorcycleType().trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de moto es obligatorio");
        }
    }

    private void normalizeRequest(PlayerRequest request) {
        request.setNickname(request.getNickname().trim().toLowerCase());
        request.setDriverName(request.getDriverName().trim());
        request.setHelmetColor(request.getHelmetColor().trim());
        request.setMotorcycleType(request.getMotorcycleType().trim());
    }

    private void validateLengths(PlayerRequest request) {
        if (request.getNickname().length() > 30) {
            throw new IllegalArgumentException("El apodo no debe superar los 30 caracteres");
        }

        if (request.getDriverName().length() > 80) {
            throw new IllegalArgumentException("El nombre del repartidor no debe superar los 80 caracteres");
        }

        if (request.getHelmetColor().length() > 30) {
            throw new IllegalArgumentException("El color del casco no debe superar los 30 caracteres");
        }

        if (request.getMotorcycleType().length() > 30) {
            throw new IllegalArgumentException("El tipo de moto no debe superar los 30 caracteres");
        }
    }

    private void validateNicknameFormat(String nickname) {
        if (nickname.contains(" ")) {
            throw new IllegalArgumentException("El apodo no debe contener espacios");
        }

        if (!nickname.matches("^[a-z0-9_]+$")) {
            throw new IllegalArgumentException("El apodo solo puede contener letras minúsculas, números y guion bajo");
        }
    }

    private void validatePlayerOptions(PlayerRequest request) {
        if (!ALLOWED_HELMET_COLORS.contains(request.getHelmetColor())) {
            throw new IllegalArgumentException(
                    "Color de casco no permitido. Opciones válidas: Negro, Rojo, Azul, Blanco, Amarillo"
            );
        }

        if (!ALLOWED_MOTORCYCLE_TYPES.contains(request.getMotorcycleType())) {
            throw new IllegalArgumentException(
                    "Tipo de moto no permitido. Opciones válidas: Estándar, Scooter, Deportiva, Retro, Delivery"
            );
        }
    }

    private void validatePlayerRequest(PlayerRequest request) {
        validateRequiredFields(request);
        normalizeRequest(request);
        validateLengths(request);
        validateNicknameFormat(request.getNickname());
        validatePlayerOptions(request);
    }

    public List<PlayerResponse> getAllPlayers() {
        return playerRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public Optional<PlayerResponse> getPlayerById(Long id) {
        return playerRepository.findById(id)
                .map(this::mapToResponse);
    }

    public Optional<PlayerResponse> getPlayerByUserAccountId(Long userAccountId) {
        return playerRepository.findByUserAccountId(userAccountId)
                .map(this::mapToResponse);
    }

    public PlayerResponse savePlayer(PlayerRequest request) {
        validatePlayerRequest(request);

        UserAccount userAccount = userAccountRepository.findById(request.getUserAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Cuenta de usuario no encontrada"));

        if (playerRepository.existsByUserAccountId(request.getUserAccountId())) {
            throw new IllegalArgumentException("Esta cuenta ya tiene una ficha de repartidor");
        }

        if (playerRepository.existsByNickname(request.getNickname())) {
            throw new IllegalArgumentException("El apodo ya está en uso");
        }

        Player player = new Player();
        player.setUserAccount(userAccount);
        player.setNickname(request.getNickname());
        player.setDriverName(request.getDriverName());
        player.setGender(request.getGender());
        player.setHelmetColor(request.getHelmetColor());
        player.setMotorcycleType(request.getMotorcycleType());

        Player savedPlayer = playerRepository.save(player);

        return mapToResponse(savedPlayer);
    }

    public Optional<PlayerResponse> updatePlayer(Long id, PlayerRequest request) {
        validatePlayerRequest(request);

        Optional<Player> optionalPlayer = playerRepository.findById(id);

        if (optionalPlayer.isEmpty()) {
            return Optional.empty();
        }

        Player existingPlayer = optionalPlayer.get();

        Optional<Player> playerWithSameNickname = playerRepository.findAll()
                .stream()
                .filter(player -> player.getNickname().equals(request.getNickname()))
                .filter(player -> !player.getId().equals(id))
                .findFirst();

        if (playerWithSameNickname.isPresent()) {
            throw new IllegalArgumentException("El apodo ya está en uso");
        }

        existingPlayer.setNickname(request.getNickname());
        existingPlayer.setDriverName(request.getDriverName());
        existingPlayer.setGender(request.getGender());
        existingPlayer.setHelmetColor(request.getHelmetColor());
        existingPlayer.setMotorcycleType(request.getMotorcycleType());

        Player updatedPlayer = playerRepository.save(existingPlayer);

        return Optional.of(mapToResponse(updatedPlayer));
    }

    public boolean deletePlayer(Long id) {
        if (!playerRepository.existsById(id)) {
            return false;
        }

        playerRepository.deleteById(id);
        return true;
    }
}