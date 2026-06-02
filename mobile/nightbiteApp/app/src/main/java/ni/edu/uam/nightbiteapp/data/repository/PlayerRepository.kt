package ni.edu.uam.nightbiteapp.data.repository

import ni.edu.uam.nightbiteapp.data.remote.ApiService
import ni.edu.uam.nightbiteapp.data.remote.RetrofitClient
import ni.edu.uam.nightbiteapp.data.remote.dto.PlayerRequest
import ni.edu.uam.nightbiteapp.data.remote.dto.PlayerResponse
import retrofit2.Response

/**
 * Repositorio encargado de gestionar las operaciones relacionadas
 * con los jugadores.
 *
 * Esta clase actúa como intermediaria entre la API remota y los futuros
 * ViewModels de la aplicación.
 */
class PlayerRepository(
    private val apiService: ApiService = RetrofitClient.apiService
) {

    /**
     * Obtiene todos los jugadores registrados desde la API.
     */
    suspend fun getPlayers(): Response<List<PlayerResponse>> {
        return apiService.getPlayers()
    }

    /**
     * Obtiene un jugador específico por su identificador.
     */
    suspend fun getPlayerById(id: Long): Response<PlayerResponse> {
        return apiService.getPlayerById(id)
    }

    /**
     * Envía los datos de un nuevo jugador hacia la API para registrarlo.
     */
    suspend fun createPlayer(playerRequest: PlayerRequest): Response<PlayerResponse> {
        return apiService.createPlayer(playerRequest)
    }
}