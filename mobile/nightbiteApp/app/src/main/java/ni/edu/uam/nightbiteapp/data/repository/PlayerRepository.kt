package ni.edu.uam.nightbiteapp.data.repository

import ni.edu.uam.nightbiteapp.data.remote.ApiService
import ni.edu.uam.nightbiteapp.data.remote.RetrofitClient
import ni.edu.uam.nightbiteapp.data.remote.dto.PlayerRequest
import ni.edu.uam.nightbiteapp.data.remote.dto.PlayerResponse
import retrofit2.Response

/**
 * Repositorio encargado de gestionar las operaciones relacionadas
 * con la ficha/personaje del repartidor.
 */
class PlayerRepository(
    private val apiService: ApiService = RetrofitClient.apiService
) {

    suspend fun getPlayers(): Response<List<PlayerResponse>> {
        return apiService.getPlayers()
    }

    suspend fun getPlayerById(id: Long): Response<PlayerResponse> {
        return apiService.getPlayerById(id)
    }

    suspend fun getPlayerByUserAccountId(userAccountId: Long): Response<PlayerResponse> {
        return apiService.getPlayerByUserAccountId(userAccountId)
    }

    suspend fun createPlayer(playerRequest: PlayerRequest): Response<PlayerResponse> {
        return apiService.createPlayer(playerRequest)
    }
}