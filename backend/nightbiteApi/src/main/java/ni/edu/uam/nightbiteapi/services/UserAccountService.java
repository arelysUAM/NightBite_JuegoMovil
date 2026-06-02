package ni.edu.uam.nightbiteapi.services;

import ni.edu.uam.nightbiteapi.dto.UserLoginRequest;
import ni.edu.uam.nightbiteapi.dto.UserRegisterRequest;
import ni.edu.uam.nightbiteapi.dto.UserResponse;
import ni.edu.uam.nightbiteapi.model.UserAccount;
import ni.edu.uam.nightbiteapi.repositories.UserAccountRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserAccountService(
            UserAccountRepository userAccountRepository,
            BCryptPasswordEncoder passwordEncoder
    ) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
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

        return mapToResponse(savedUser);
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

        return mapToResponse(user);
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

    private UserResponse mapToResponse(UserAccount user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getAge(),
                user.getCreatedAt()
        );
    }
}