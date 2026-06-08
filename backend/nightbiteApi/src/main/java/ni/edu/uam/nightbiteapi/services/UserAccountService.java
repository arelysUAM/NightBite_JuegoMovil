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
import ni.edu.uam.nightbiteapi.dto.UpdatePasswordRequest;
import ni.edu.uam.nightbiteapi.dto.UpdateUsernameRequest;

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
            throw new RuntimeException("Campos incompletos");
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new RuntimeException("Campos incompletos");
        }

        UserAccount user = userAccountRepository
                .findByUsernameOrEmail(
                        request.getUsernameOrEmail().trim(),
                        request.getUsernameOrEmail().trim()
                )
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        boolean passwordMatches = passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash()
        );

        if (!passwordMatches) {
            throw new RuntimeException("Usuario o contraseña incorrectos");
        }

        return mapToResponseWithPlayer(user);
    }

    public UserResponse updateUsername(Long id, UpdateUsernameRequest request) {
        UserAccount user = userAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta de usuario no encontrada"));

        validateUpdateUsernameRequest(request);

        String newUsername = request.getNewUsername().trim();

        if (user.getUsername().equals(newUsername)) {
            throw new RuntimeException("El nuevo nombre de usuario debe ser diferente al actual");
        }

        if (userAccountRepository.existsByUsername(newUsername)) {
            throw new RuntimeException("El nombre de usuario ya está registrado");
        }

        user.setUsername(newUsername);

        UserAccount updatedUser = userAccountRepository.save(user);

        return mapToResponseWithPlayer(updatedUser);
    }

    public void updatePassword(Long id, UpdatePasswordRequest request) {
        UserAccount user = userAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta de usuario no encontrada"));

        validateUpdatePasswordRequest(request);

        boolean currentPasswordMatches = passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPasswordHash()
        );

        if (!currentPasswordMatches) {
            throw new RuntimeException("La contraseña actual no es correcta");
        }

        boolean samePassword = passwordEncoder.matches(
                request.getNewPassword(),
                user.getPasswordHash()
        );

        if (samePassword) {
            throw new RuntimeException("La nueva contraseña debe ser diferente a la contraseña actual");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));

        userAccountRepository.save(user);
    }

    private void validateUpdateUsernameRequest(UpdateUsernameRequest request) {
        if (request.getNewUsername() == null || request.getNewUsername().isBlank()) {
            throw new RuntimeException("El nuevo nombre de usuario es obligatorio");
        }

        String newUsername = request.getNewUsername().trim();

        if (newUsername.contains(" ")) {
            throw new RuntimeException("El nombre de usuario no debe contener espacios");
        }

        if (!newUsername.equals(newUsername.toLowerCase())) {
            throw new RuntimeException("El nombre de usuario debe estar en minúsculas");
        }

        if (newUsername.length() > 16) {
            throw new RuntimeException("El nombre de usuario no debe superar los 16 caracteres");
        }

        if (!newUsername.matches("^[a-z0-9_]+$")) {
            throw new RuntimeException("El nombre de usuario solo puede contener letras minúsculas, números y guion bajo");
        }
    }

    private void validateUpdatePasswordRequest(UpdatePasswordRequest request) {
        if (request.getCurrentPassword() == null || request.getCurrentPassword().isBlank()) {
            throw new RuntimeException("La contraseña actual es obligatoria");
        }

        if (request.getNewPassword() == null || request.getNewPassword().isBlank()) {
            throw new RuntimeException("La nueva contraseña es obligatoria");
        }

        if (request.getConfirmPassword() == null || request.getConfirmPassword().isBlank()) {
            throw new RuntimeException("La confirmación de contraseña es obligatoria");
        }

        if (request.getNewPassword().length() < 8) {
            throw new RuntimeException("La nueva contraseña debe tener al menos 8 caracteres");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("La nueva contraseña y la confirmación no coinciden");
        }
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

        if (request.getPassword().length() < 8) {
            throw new RuntimeException("La contraseña debe tener al menos 8 caracteres");
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