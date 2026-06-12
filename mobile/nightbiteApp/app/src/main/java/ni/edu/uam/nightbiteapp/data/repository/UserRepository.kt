package ni.edu.uam.nightbiteapp.data.repository

import ni.edu.uam.nightbiteapp.data.remote.ApiService
import ni.edu.uam.nightbiteapp.data.remote.RetrofitClient
import ni.edu.uam.nightbiteapp.data.remote.dto.MessageResponse
import ni.edu.uam.nightbiteapp.data.remote.dto.UserLoginRequest
import ni.edu.uam.nightbiteapp.data.remote.dto.UserRegisterRequest
import ni.edu.uam.nightbiteapp.data.remote.dto.UserResponse
import retrofit2.Response
import ni.edu.uam.nightbiteapp.data.remote.dto.UpdatePasswordRequest
import ni.edu.uam.nightbiteapp.data.remote.dto.UpdateUsernameRequest
import ni.edu.uam.nightbiteapp.data.remote.dto.AuthResponse

/**
 * Repositorio encargado de gestionar las operaciones relacionadas
 * con cuentas de usuario.
 *
 * UserAccount representa la cuenta real del jugador, mientras que Player
 * representa la ficha/personaje del repartidor dentro del juego.
 */
class UserRepository(
    private val apiService: ApiService = RetrofitClient.apiService
) {

    /**
     * Registra una cuenta de usuario en la API.
     */
    suspend fun registerUser(
        userRegisterRequest: UserRegisterRequest
    ): Response<UserResponse> {
        return apiService.registerUser(userRegisterRequest)
    }

    /**
     * Inicia sesión con una cuenta existente.
     */
    suspend fun loginUser(
        userLoginRequest: UserLoginRequest
    ): Response<AuthResponse> {
        return apiService.loginUser(userLoginRequest)
    }

    /**
     * Obtiene todas las cuentas de usuario registradas.
     *
     * Este método puede servir para pruebas o administración,
     * pero no debería usarse como forma principal para cargar
     * el perfil del usuario activo.
     */
    suspend fun getUsers(): Response<List<UserResponse>> {
        return apiService.getUsers()
    }

    /**
     * Obtiene una cuenta de usuario específica por su id.
     *
     * Este método será usado por ProfileViewModel para cargar
     * el perfil actualizado del usuario autenticado, incluyendo
     * su Player asignado si existe.
     */
    suspend fun getUserById(id: Long): Response<UserResponse> {
        return apiService.getUserById(id)
    }

    /**
     * Actualiza el nombre de usuario de una cuenta.
     */
    suspend fun updateUsername(
        userId: Long,
        request: UpdateUsernameRequest
    ): Response<UserResponse> {
        return apiService.updateUsername(userId, request)
    }

    /**
     * Actualiza la contraseña de una cuenta.
     */
    suspend fun updatePassword(
        userId: Long,
        request: UpdatePasswordRequest
    ): Response<UserResponse> {
        return apiService.updatePassword(userId, request)
    }

    suspend fun checkHealth(): Response<MessageResponse> {
        return apiService.checkHealth()
    }
}