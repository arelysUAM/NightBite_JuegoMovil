package ni.edu.uam.nightbiteapi.controllers;

import jakarta.validation.Valid;
import ni.edu.uam.nightbiteapi.dto.MessageResponse;
import ni.edu.uam.nightbiteapi.dto.UpdateAccountInfoRequest;
import ni.edu.uam.nightbiteapi.dto.UpdatePasswordRequest;
import ni.edu.uam.nightbiteapi.dto.UpdateUsernameRequest;
import ni.edu.uam.nightbiteapi.dto.UserLoginRequest;
import ni.edu.uam.nightbiteapi.dto.UserRegisterRequest;
import ni.edu.uam.nightbiteapi.dto.UserResponse;
import ni.edu.uam.nightbiteapi.dto.UsernameAvailabilityResponse;
import ni.edu.uam.nightbiteapi.services.UserAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de cuentas de usuario.
 *
 * Para el entregable no se utiliza JWT.
 * El login devuelve directamente los datos públicos del usuario.
 */
@RestController
@RequestMapping("/api/users")
public class UserAccountController {

    private final UserAccountService userAccountService;

    public UserAccountController(
            UserAccountService userAccountService
    ) {
        this.userAccountService = userAccountService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userAccountService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(
            @PathVariable Long id
    ) {
        return userAccountService.getUserById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new MessageResponse("Cuenta de usuario no encontrada")));
    }

    @GetMapping("/check-username/{username}")
    public ResponseEntity<UsernameAvailabilityResponse> checkUsernameAvailability(
            @PathVariable String username
    ) {
        UsernameAvailabilityResponse response =
                userAccountService.checkUsernameAvailability(username);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(
            @Valid @RequestBody UserRegisterRequest request
    ) {
        UserResponse response = userAccountService.registerUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> loginUser(
            @Valid @RequestBody UserLoginRequest request
    ) {
        UserResponse response = userAccountService.loginUser(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/username")
    public ResponseEntity<UserResponse> updateUsername(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUsernameRequest request
    ) {
        UserResponse response = userAccountService.updateUsername(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/account-info")
    public ResponseEntity<UserResponse> updateAccountInfo(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAccountInfoRequest request
    ) {
        UserResponse response = userAccountService.updateAccountInfo(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<MessageResponse> updatePassword(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePasswordRequest request
    ) {
        userAccountService.updatePassword(id, request);

        return ResponseEntity.ok(
                new MessageResponse("Contraseña actualizada correctamente")
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteUser(
            @PathVariable Long id
    ) {
        boolean deleted = userAccountService.deleteUser(id);

        if (!deleted) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Cuenta de usuario no encontrada"));
        }

        return ResponseEntity.ok(
                new MessageResponse("Cuenta de usuario eliminada correctamente")
        );
    }
}