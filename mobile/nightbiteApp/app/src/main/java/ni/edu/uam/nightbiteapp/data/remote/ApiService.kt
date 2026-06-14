package ni.edu.uam.nightbiteapp.data.remote

import ni.edu.uam.nightbiteapp.data.remote.dto.AuthResponse
import ni.edu.uam.nightbiteapp.data.remote.dto.PlayerRequest
import ni.edu.uam.nightbiteapp.data.remote.dto.PlayerResponse
import ni.edu.uam.nightbiteapp.data.remote.dto.UserLoginRequest
import ni.edu.uam.nightbiteapp.data.remote.dto.UserRegisterRequest
import ni.edu.uam.nightbiteapp.data.remote.dto.UserResponse
import ni.edu.uam.nightbiteapp.data.remote.dto.UpdatePasswordRequest
import ni.edu.uam.nightbiteapp.data.remote.dto.UpdateUsernameRequest
import retrofit2.http.PUT
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ni.edu.uam.nightbiteapp.data.remote.dto.MessageResponse
import retrofit2.http.DELETE

/**
 * Define los endpoints remotos que Android consumirá desde la API de Spring Boot.
 */
interface ApiService {

    @GET("api/players")
    suspend fun getPlayers(): Response<List<PlayerResponse>>

    @GET("api/players/{id}")
    suspend fun getPlayerById(
        @Path("id") id: Long
    ): Response<PlayerResponse>

    @GET("api/players/account/{userAccountId}")
    suspend fun getPlayerByUserAccountId(
        @Path("userAccountId") userAccountId: Long
    ): Response<PlayerResponse>

    @POST("api/players")
    suspend fun createPlayer(
        @Body playerRequest: PlayerRequest
    ): Response<PlayerResponse>

    @GET("api/users")
    suspend fun getUsers(): Response<List<UserResponse>>

    @GET("api/users/{id}")
    suspend fun getUserById(
        @Path("id") id: Long
    ): Response<UserResponse>

    @GET("api/health")
    suspend fun checkHealth(): Response<MessageResponse>

    @POST("api/users/register")
    suspend fun registerUser(
        @Body userRegisterRequest: UserRegisterRequest
    ): Response<UserResponse>

    @POST("api/users/login")
    suspend fun loginUser(
        @Body userLoginRequest: UserLoginRequest
    ): Response<AuthResponse>

    @PUT("api/users/{id}/username")
    suspend fun updateUsername(
        @Path("id") id: Long,
        @Body request: UpdateUsernameRequest
    ): Response<UserResponse>

    @PUT("api/users/{id}/password")
    suspend fun updatePassword(
        @Path("id") id: Long,
        @Body request: UpdatePasswordRequest
    ): Response<UserResponse>

    @DELETE("api/users/{id}")
    suspend fun deleteUser(
        @Path("id") id: Long
    ): Response<MessageResponse>
}