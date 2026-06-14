package ni.edu.uam.nightbiteapp.data.remote

import ni.edu.uam.nightbiteapp.BuildConfig
import ni.edu.uam.nightbiteapp.data.local.session.SessionManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Cliente de Retrofit para consumir la API de NightBite.
 *
 * La URL base se obtiene desde BuildConfig para separar
 * ambiente local de desarrollo y ambiente de producción.
 *
 * También permite inicializar un AuthInterceptor para enviar
 * automáticamente el token JWT guardado en la sesión local.
 */
object RetrofitClient {

    @Volatile
    private var initializedApiService: ApiService? = null

    /**
     * ApiService usado por los repositorios.
     *
     * Si la app ya llamó initialize(sessionManager), usará Retrofit con token.
     * Si no, crea una versión básica sin interceptor para evitar errores en previews
     * o pruebas aisladas.
     */
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
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    private fun createApiServiceWithoutAuth(): ApiService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}