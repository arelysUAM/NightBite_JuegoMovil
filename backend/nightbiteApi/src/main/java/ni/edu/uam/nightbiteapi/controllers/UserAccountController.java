package ni.edu.uam.nightbiteapi.controllers;

import ni.edu.uam.nightbiteapi.dto.MessageResponse;
import ni.edu.uam.nightbiteapi.dto.UserLoginRequest;
import ni.edu.uam.nightbiteapi.dto.UserRegisterRequest;
import ni.edu.uam.nightbiteapi.dto.UserResponse;
import ni.edu.uam.nightbiteapi.services.UserAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ni.edu.uam.nightbiteapi.dto.UpdatePasswordRequest;
import ni.edu.uam.nightbiteapi.dto.UpdateUsernameRequest;

import java.util.List;

/**
 * Controlador REST para la gestión de cuentas de usuario.
 */
@RestController
@RequestMapping("/api/users")
public class UserAccountController {

    private final UserAccountService userAccountService;

    public UserAccountController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    /**
     * Endpoint para obtener todas las cuentas de usuario.
     *
     * Método HTTP: GET
     * URL: /api/users
     */
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userAccountService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Endpoint para obtener una cuenta de usuario por su id.
     *
     * Método HTTP: GET
     * URL: /api/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return userAccountService.getUserById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new MessageResponse("Cuenta de usuario no encontrada")));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterRequest request) {
        try {
            UserResponse response = userAccountService.registerUser(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginRequest request) {
        try {
            UserResponse response = userAccountService.loginUser(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse(e.getMessage()));
        }
    }

    /**
     * Endpoint para cambiar el nombre de usuario de una cuenta.
     *
     * Método HTTP: PUT
     * URL: /api/users/{id}/username
     */
    @PutMapping("/{id}/username")
    public ResponseEntity<?> updateUsername(
            @PathVariable Long id,
            @RequestBody UpdateUsernameRequest request
    ) {
        try {
            UserResponse response = userAccountService.updateUsername(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(e.getMessage()));
        }
    }

    /**
     * Endpoint para cambiar la contraseña de una cuenta.
     *
     * Método HTTP: PUT
     * URL: /api/users/{id}/password
     */
    @PutMapping("/{id}/password")
    public ResponseEntity<?> updatePassword(
            @PathVariable Long id,
            @RequestBody UpdatePasswordRequest request
    ) {
        try {
            userAccountService.updatePassword(id, request);
            return ResponseEntity.ok(new MessageResponse("Contraseña actualizada correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(e.getMessage()));
        }
    }

    /**
     * Endpoint para eliminar una cuenta de usuario.
     *
     * Método HTTP: DELETE
     * URL: /api/users/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable Long id) {
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