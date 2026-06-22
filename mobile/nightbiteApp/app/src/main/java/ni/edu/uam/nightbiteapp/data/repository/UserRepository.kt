package ni.edu.uam.nightbiteapp.data.repository

import ni.edu.uam.nightbiteapp.data.remote.ApiService
import ni.edu.uam.nightbiteapp.data.remote.RetrofitClient
import ni.edu.uam.nightbiteapp.data.remote.dto.MessageResponse
import ni.edu.uam.nightbiteapp.data.remote.dto.UpdateAccountInfoRequest
import ni.edu.uam.nightbiteapp.data.remote.dto.UpdatePasswordRequest
import ni.edu.uam.nightbiteapp.data.remote.dto.UpdateUsernameRequest
import ni.edu.uam.nightbiteapp.data.remote.dto.UserLoginRequest
import ni.edu.uam.nightbiteapp.data.remote.dto.UserRegisterRequest
import ni.edu.uam.nightbiteapp.data.remote.dto.UserResponse
import ni.edu.uam.nightbiteapp.data.remote.dto.UsernameAvailabilityResponse
import retrofit2.Response

/**
 * Repositorio encargado de gestionar las operaciones relacionadas
 * con cuentas de usuario.
 *
 * Importante:
 * No guardamos una instancia fija de ApiService, porque RetrofitClient
 * puede inicializarse después con AuthInterceptor y SessionManager.
 */
class UserRepository(
    private val apiServiceProvider: () -> ApiService = {
        RetrofitClient.apiService
    }
) {
    private val apiService: ApiService
        get() = apiServiceProvider()

    suspend fun registerUser(
        userRegisterRequest: UserRegisterRequest
    ): Response<UserResponse> {
        return apiService.registerUser(userRegisterRequest)
    }

    suspend fun checkUsernameAvailability(
        username: String
    ): Response<UsernameAvailabilityResponse> {
        return apiService.checkUsernameAvailability(username)
    }

    suspend fun loginUser(
        userLoginRequest: UserLoginRequest
    ): Response<UserResponse> {
        return apiService.loginUser(userLoginRequest)
    }

    suspend fun getUsers(): Response<List<UserResponse>> {
        return apiService.getUsers()
    }

    suspend fun getUserById(id: Long): Response<UserResponse> {
        return apiService.getUserById(id)
    }

    suspend fun updateUsername(
        userId: Long,
        request: UpdateUsernameRequest
    ): Response<UserResponse> {
        return apiService.updateUsername(userId, request)
    }

    suspend fun updateAccountInfo(
        userId: Long,
        request: UpdateAccountInfoRequest
    ): Response<UserResponse> {
        return apiService.updateAccountInfo(userId, request)
    }

    suspend fun updatePassword(
        userId: Long,
        request: UpdatePasswordRequest
    ): Response<UserResponse> {
        return apiService.updatePassword(userId, request)
    }

    suspend fun checkHealth(): Response<MessageResponse> {
        return apiService.checkHealth()
    }

    suspend fun deleteUser(userId: Long): Response<MessageResponse> {
        return apiService.deleteUser(userId)
    }
}