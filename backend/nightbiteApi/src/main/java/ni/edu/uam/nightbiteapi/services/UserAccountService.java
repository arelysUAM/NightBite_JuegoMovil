package ni.edu.uam.nightbiteapi.services;

import ni.edu.uam.nightbiteapi.dto.PlayerSummaryResponse;
import ni.edu.uam.nightbiteapi.dto.UpdateAccountInfoRequest;
import ni.edu.uam.nightbiteapi.dto.UpdatePasswordRequest;
import ni.edu.uam.nightbiteapi.dto.UpdateUsernameRequest;
import ni.edu.uam.nightbiteapi.dto.UserLoginRequest;
import ni.edu.uam.nightbiteapi.dto.UserRegisterRequest;
import ni.edu.uam.nightbiteapi.dto.UserResponse;
import ni.edu.uam.nightbiteapi.dto.UsernameAvailabilityResponse;
import ni.edu.uam.nightbiteapi.dto.VerifyCurrentPasswordRequest;
import ni.edu.uam.nightbiteapi.model.Player;
import ni.edu.uam.nightbiteapi.model.UserAccount;
import ni.edu.uam.nightbiteapi.repositories.LevelResultRepository;
import ni.edu.uam.nightbiteapi.repositories.PlayerProgressRepository;
import ni.edu.uam.nightbiteapi.repositories.PlayerRepository;
import ni.edu.uam.nightbiteapi.repositories.UserAccountRepository;
import ni.edu.uam.nightbiteapi.repositories.UserBadgeRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final PlayerRepository playerRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final LevelResultRepository levelResultRepository;
    private final UserBadgeRepository userBadgeRepository;
    private final PlayerProgressRepository playerProgressRepository;

    public UserAccountService(
            UserAccountRepository userAccountRepository,
            PlayerRepository playerRepository,
            BCryptPasswordEncoder passwordEncoder,
            LevelResultRepository levelResultRepository,
            UserBadgeRepository userBadgeRepository,
            PlayerProgressRepository playerProgressRepository
    ) {
        this.userAccountRepository = userAccountRepository;
        this.playerRepository = playerRepository;
        this.passwordEncoder = passwordEncoder;
        this.levelResultRepository = levelResultRepository;
        this.userBadgeRepository = userBadgeRepository;
        this.playerProgressRepository = playerProgressRepository;
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

    public UsernameAvailabilityResponse checkUsernameAvailability(String username) {
        validateUsername(username);

        String normalizedUsername = username.trim().toLowerCase();

        boolean available = !userAccountRepository.existsByUsername(normalizedUsername);

        String message = available
                ? "Nombre de usuario disponible."
                : "Nombre de usuario no disponible.";

        return new UsernameAvailabilityResponse(
                normalizedUsername,
                available,
                message
        );
    }

    public UserResponse registerUser(UserRegisterRequest request) {
        validateRegisterRequest(request);

        String username = request.getUsername().trim().toLowerCase();
        String email = request.getEmail().trim().toLowerCase();

        if (userAccountRepository.existsByUsername(username)) {
            throw new RuntimeException("El nombre de usuario ya está registrado");
        }

        if (userAccountRepository.existsByEmail(email)) {
            throw new RuntimeException("El correo ya está registrado");
        }

        UserAccount user = new UserAccount();
        user.setUsername(username);
        user.setEmail(email);
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

        String usernameOrEmail = request.getUsernameOrEmail().trim().toLowerCase();

        UserAccount user = userAccountRepository
                .findByUsernameOrEmail(
                        usernameOrEmail,
                        usernameOrEmail
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

        String newUsername = request.getNewUsername().trim().toLowerCase();

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

    public UserResponse updateAccountInfo(
            Long id,
            UpdateAccountInfoRequest request
    ) {
        UserAccount user = userAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta de usuario no encontrada"));

        validateUpdateAccountInfoRequest(request);

        String newUsername = request.getUsername().trim().toLowerCase();

        if (!user.getUsername().equals(newUsername)
                && userAccountRepository.existsByUsername(newUsername)) {
            throw new RuntimeException("El nombre de usuario ya está registrado");
        }

        Player player = playerRepository.findByUserAccountId(id)
                .orElseThrow(() -> new RuntimeException("Ficha de repartidor no encontrada para esta cuenta"));

        user.setUsername(newUsername);
        user.setAge(request.getAge());

        player.setGender(request.getGender());

        UserAccount updatedUser = userAccountRepository.save(user);
        playerRepository.save(player);

        return mapToResponseWithPlayer(updatedUser);
    }

    public void verifyCurrentPassword(
            Long id,
            VerifyCurrentPasswordRequest request
    ) {
        UserAccount user = userAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta de usuario no encontrada"));

        if (request.getCurrentPassword() == null || request.getCurrentPassword().isBlank()) {
            throw new RuntimeException("La contraseña actual es obligatoria");
        }

        boolean currentPasswordMatches = passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPasswordHash()
        );

        if (!currentPasswordMatches) {
            throw new RuntimeException("La contraseña actual no es correcta");
        }
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

    @Transactional
    public boolean deleteUser(Long id) {
        Optional<UserAccount> optionalUser = userAccountRepository.findById(id);

        if (optionalUser.isEmpty()) {
            return false;
        }

        levelResultRepository.deleteByUserAccountId(id);
        userBadgeRepository.deleteByUserAccountId(id);
        playerProgressRepository.deleteByUserAccountId(id);

        userAccountRepository.delete(optionalUser.get());

        return true;
    }

    private void validateUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new RuntimeException("El nombre de usuario es obligatorio");
        }

        String normalizedUsername = username.trim();

        if (normalizedUsername.contains(" ")) {
            throw new RuntimeException("El nombre de usuario no debe contener espacios");
        }

        if (!normalizedUsername.equals(normalizedUsername.toLowerCase())) {
            throw new RuntimeException("El nombre de usuario debe estar en minúsculas");
        }

        if (normalizedUsername.length() < 4) {
            throw new RuntimeException("El nombre de usuario debe tener al menos 4 caracteres");
        }

        if (normalizedUsername.length() > 16) {
            throw new RuntimeException("El nombre de usuario no debe superar los 16 caracteres");
        }

        if (!normalizedUsername.matches("^[a-z0-9_]+$")) {
            throw new RuntimeException("El nombre de usuario solo puede contener letras minúsculas, números y guion bajo");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new RuntimeException("El correo es obligatorio");
        }

        String normalizedEmail = email.trim();

        if (normalizedEmail.contains(" ")) {
            throw new RuntimeException("El correo no debe contener espacios");
        }

        if (!normalizedEmail.equals(normalizedEmail.toLowerCase())) {
            throw new RuntimeException("El correo debe estar en minúsculas");
        }

        if (normalizedEmail.length() > 100) {
            throw new RuntimeException("El correo no debe superar los 100 caracteres");
        }

        if (!normalizedEmail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new RuntimeException("Ingresa un correo electrónico válido");
        }
    }

    private void validatePassword(String password, String fieldName) {
        if (password == null || password.isBlank()) {
            throw new RuntimeException(fieldName + " es obligatoria");
        }

        if (password.length() < 8) {
            throw new RuntimeException(fieldName + " debe tener al menos 8 caracteres");
        }

        if (password.length() > 20) {
            throw new RuntimeException(fieldName + " no debe superar los 20 caracteres");
        }

        if (password.startsWith(" ") || password.endsWith(" ")) {
            throw new RuntimeException(fieldName + " no puede iniciar ni terminar con espacios");
        }
    }

    private void validateAge(Integer age) {
        if (age == null) {
            throw new RuntimeException("La edad es obligatoria");
        }

        if (age < 13) {
            throw new RuntimeException("Debes tener 13 años o más para crear una cuenta");
        }

        if (age > 120) {
            throw new RuntimeException("La edad no puede ser mayor a 120 años");
        }
    }

    private void validateUpdateUsernameRequest(UpdateUsernameRequest request) {
        validateUsername(request.getNewUsername());
    }

    private void validateUpdateAccountInfoRequest(UpdateAccountInfoRequest request) {
        validateUsername(request.getUsername());
        validateAge(request.getAge());

        if (request.getGender() == null) {
            throw new RuntimeException("El género es obligatorio");
        }
    }

    private void validateUpdatePasswordRequest(UpdatePasswordRequest request) {
        if (request.getCurrentPassword() == null || request.getCurrentPassword().isBlank()) {
            throw new RuntimeException("La contraseña actual es obligatoria");
        }

        validatePassword(request.getNewPassword(), "La nueva contraseña");

        if (request.getConfirmNewPassword() == null || request.getConfirmNewPassword().isBlank()) {
            throw new RuntimeException("La confirmación de contraseña es obligatoria");
        }

        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new RuntimeException("La nueva contraseña y la confirmación no coinciden");
        }
    }

    private void validateRegisterRequest(UserRegisterRequest request) {
        validateUsername(request.getUsername());
        validateEmail(request.getEmail());
        validatePassword(request.getPassword(), "La contraseña");
        validateAge(request.getAge());
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
                player.getDriverName(),
                player.getGender(),
                player.getHelmetColor(),
                player.getMotorcycleType()
        );
    }
}