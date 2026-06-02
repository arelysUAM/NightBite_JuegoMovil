package ni.edu.uam.nightbiteapp.data.repository

import ni.edu.uam.nightbiteapp.data.remote.ApiService
import ni.edu.uam.nightbiteapp.data.remote.RetrofitClient
import ni.edu.uam.nightbiteapp.data.remote.dto.UserLoginRequest
import ni.edu.uam.nightbiteapp.data.remote.dto.UserRegisterRequest
import ni.edu.uam.nightbiteapp.data.remote.dto.UserResponse
import retrofit2.Response

/**
 * Repositorio encargado de gestionar las operaciones relacionadas
 * con cuentas de usuario.
 *
 * UserAccount representa la cuenta real del jugador, mientras que Player
 * se usará más adelante para el personaje o avatar dentro del juego.
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
    ): Response<UserResponse> {
        return apiService.loginUser(userLoginRequest)
    }
}