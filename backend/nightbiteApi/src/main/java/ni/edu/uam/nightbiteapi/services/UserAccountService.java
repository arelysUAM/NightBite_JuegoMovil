package ni.edu.uam.nightbiteapi.services;

import ni.edu.uam.nightbiteapi.dto.PlayerSummaryResponse;
import ni.edu.uam.nightbiteapi.dto.UserLoginRequest;
import ni.edu.uam.nightbiteapi.dto.UserRegisterRequest;
import ni.edu.uam.nightbiteapi.dto.UserResponse;
import ni.edu.uam.nightbiteapi.model.Player;
import ni.edu.uam.nightbiteapi.model.UserAccount;
import ni.edu.uam.nightbiteapi.repositories.PlayerRepository;
import ni.edu.uam.nightbiteapi.repositories.UserAccountRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final PlayerRepository playerRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserAccountService(
            UserAccountRepository userAccountRepository,
            PlayerRepository playerRepository,
            BCryptPasswordEncoder passwordEncoder
    ) {
        this.userAccountRepository = userAccountRepository;
        this.playerRepository = playerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserResponse> getAllUsers() {
        return userAccountRepository.findAll()
                .stream()
                .map(this::mapToResponseWithPlayer)
                .toList();
    }

    public Optional<UserResponse> getUserById(Long id) {
        return userAccountRepository.findById(id)
                .map(this::mapToResponseWithPlayer);
    }

    public UserResponse registerUser(UserRegisterRequest request) {
        validateRegisterRequest(request);

        if (userAccountRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya está registrado");
        }

        if (userAccountRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El correo ya está registrado");
        }

        UserAccount user = new UserAccount();
        user.setUsername(request.getUsername().trim());
        user.setEmail(request.getEmail().trim());
        user.setAge(request.getAge());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        UserAccount savedUser = userAccountRepository.save(user);

        return mapToResponseWithPlayer(savedUser);
    }

    public UserResponse loginUser(UserLoginRequest request) {
        if (request.getUsernameOrEmail() == null || request.getUsernameOrEmail().isBlank()) {
            throw new RuntimeException("Ingresa usuario o correo");
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new RuntimeException("Ingresa la contraseña");
        }

        UserAccount user = userAccountRepository
                .findByUsernameOrEmail(
                        request.getUsernameOrEmail().trim(),
                        request.getUsernameOrEmail().trim()
                )
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        boolean passwordMatches = passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash()
        );

        if (!passwordMatches) {
            throw new RuntimeException("Credenciales inválidas");
        }

        return mapToResponseWithPlayer(user);
    }

    private void validateRegisterRequest(UserRegisterRequest request) {
        if (request.getUsername() == null || request.getUsername().isBlank()) {
            throw new RuntimeException("El nombre de usuario es obligatorio");
        }

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new RuntimeException("El correo es obligatorio");
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new RuntimeException("La contraseña es obligatoria");
        }

        if (request.getPassword().length() < 6) {
            throw new RuntimeException("La contraseña debe tener al menos 6 caracteres");
        }

        if (request.getAge() == null) {
            throw new RuntimeException("La edad es obligatoria");
        }

        if (request.getAge() < 13) {
            throw new RuntimeException("Debes tener 13 años o más para crear una cuenta");
        }
    }

    private UserResponse mapToResponseWithPlayer(UserAccount user) {
        PlayerSummaryResponse playerSummary = playerRepository
                .findByUserAccountId(user.getId())
                .map(this::mapPlayerToSummary)
                .orElse(null);

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getAge(),
                user.getCreatedAt(),
                playerSummary
        );
    }

    private PlayerSummaryResponse mapPlayerToSummary(Player player) {
        return new PlayerSummaryResponse(
                player.getId(),
                player.getNickname(),
                player.getDriverName(),
                player.getGender(),
                player.getHelmetColor(),
                player.getMotorcycleType()
        );
    }
}