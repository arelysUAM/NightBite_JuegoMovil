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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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
        validateAuthenticatedUserOwnsAccount(id);

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
        user.setUsername(request.getUsername().trim().toLowerCase());
        user.setEmail(request.getEmail().trim().toLowerCase());
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
        validateAuthenticatedUserOwnsAccount(id);

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

    public void updatePassword(Long id, UpdatePasswordRequest request) {
        validateAuthenticatedUserOwnsAccount(id);

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

    public boolean deleteUser(Long id) {
        validateAuthenticatedUserOwnsAccount(id);

        Optional<UserAccount> optionalUser = userAccountRepository.findById(id);

        if (optionalUser.isEmpty()) {
            return false;
        }

        UserAccount user = optionalUser.get();

        userAccountRepository.delete(user);

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

        if (password.length() > 50) {
            throw new RuntimeException(fieldName + " no debe superar los 50 caracteres");
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
    }

    private void validateUpdateUsernameRequest(UpdateUsernameRequest request) {
        validateUsername(request.getNewUsername());
    }

    private void validateUpdatePasswordRequest(UpdatePasswordRequest request) {
        validatePassword(request.getCurrentPassword(), "La contraseña actual");
        validatePassword(request.getNewPassword(), "La nueva contraseña");

        if (request.getConfirmPassword() == null || request.getConfirmPassword().isBlank()) {
            throw new RuntimeException("La confirmación de contraseña es obligatoria");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("La nueva contraseña y la confirmación no coinciden");
        }
    }

    private void validateRegisterRequest(UserRegisterRequest request) {
        validateUsername(request.getUsername());
        validateEmail(request.getEmail());
        validatePassword(request.getPassword(), "La contraseña");
        validateAge(request.getAge());
    }

    private void validateAuthenticatedUserOwnsAccount(Long requestedUserId) {
        if (requestedUserId == null) {
            throw new AccessDeniedException("No tienes permiso para acceder a esta cuenta");
        }

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Debes iniciar sesión para acceder a este recurso");
        }

        String authenticatedUserIdText = authentication.getName();

        try {
            Long authenticatedUserId = Long.parseLong(authenticatedUserIdText);

            if (!authenticatedUserId.equals(requestedUserId)) {
                throw new AccessDeniedException("No tienes permiso para acceder a esta cuenta");
            }
        } catch (NumberFormatException e) {
            throw new AccessDeniedException("Token inválido");
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