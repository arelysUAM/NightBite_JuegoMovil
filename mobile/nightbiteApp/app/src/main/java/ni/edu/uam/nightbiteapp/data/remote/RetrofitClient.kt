package ni.edu.uam.nightbiteapp.data.remote

import ni.edu.uam.nightbiteapp.data.local.session.SessionManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Cliente de Retrofit para consumir la API de NightBite.
 *
 * En esta etapa del proyecto, la API se ejecuta localmente en la computadora
 * y la app se conecta usando la IP de la PC dentro de la misma red Wi-Fi.
 */
object RetrofitClient {

    private const val BASE_URL = "http://192.168.1.12:8080/"

    @Volatile
    private var initializedApiService: ApiService? = null

    val apiService: ApiService
        get() = initializedApiService ?: createApiServiceWithoutAuth()

    fun initialize(sessionManager: SessionManager) {
        if (initializedApiService != null) return

        synchronized(this) {
            if (initializedApiService == null) {
                initializedApiService = createApiServiceWithAuth(sessionManager)
            }
        }
    }

    private fun createApiServiceWithAuth(
        sessionManager: SessionManager
    ): ApiService {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(sessionManager))
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    private fun createApiServiceWithoutAuth(): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}