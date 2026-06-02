package ni.edu.uam.nightbiteapp.data.remote

import ni.edu.uam.nightbiteapp.data.remote.dto.PlayerRequest
import ni.edu.uam.nightbiteapp.data.remote.dto.PlayerResponse
import ni.edu.uam.nightbiteapp.data.remote.dto.UserLoginRequest
import ni.edu.uam.nightbiteapp.data.remote.dto.UserRegisterRequest
import ni.edu.uam.nightbiteapp.data.remote.dto.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Define los endpoints remotos que Android consumirá desde la API de Spring Boot.
 *
 * Esta interfaz será utilizada por Retrofit para enviar y recibir información
 * relacionada con los jugadores del sistema NightBite.
 */
interface ApiService {

    /**
     * Obtiene la lista de jugadores registrados.
     *
     * Endpoint esperado en backend:
     * GET /api/players
     */
    @GET("api/players")
    suspend fun getPlayers(): Response<List<PlayerResponse>>

    /**
     * Obtiene un jugador específico por su identificador.
     *
     * Endpoint esperado en backend:
     * GET /api/players/{id}
     */
    @GET("api/players/{id}")
    suspend fun getPlayerById(
        @Path("id") id: Long
    ): Response<PlayerResponse>

    /**
     * Registra un nuevo jugador en la API.
     *
     * Endpoint esperado en backend:
     * POST /api/players
     */
    @POST("api/players")
    suspend fun createPlayer(
        @Body playerRequest: PlayerRequest
    ): Response<PlayerResponse>

    /**
     * Registra una nueva cuenta de usuario.
     *
     * Endpoint esperado:
     * POST /api/users/register
     */
    @POST("api/users/register")
    suspend fun registerUser(
        @Body userRegisterRequest: UserRegisterRequest
    ): Response<UserResponse>

    /**
     * Inicia sesión con una cuenta existente.
     *
     * Endpoint esperado:
     * POST /api/users/login
     */
    @POST("api/users/login")
    suspend fun loginUser(
        @Body userLoginRequest: UserLoginRequest
    ): Response<UserResponse>
}