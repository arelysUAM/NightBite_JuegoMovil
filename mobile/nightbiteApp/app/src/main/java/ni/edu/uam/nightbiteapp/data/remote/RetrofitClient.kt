package ni.edu.uam.nightbiteapp.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Cliente de Retrofit para consumir la API de NightBite.
 *
 * Para el entregable no se usa JWT ni AuthInterceptor.
 * Todas las peticiones se hacen directamente a la API.
 */
object RetrofitClient {

    private const val BASE_URL = "http://10.150.160.86:8080/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}