package ni.edu.uam.nightbiteapp.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Cliente de Retrofit para consumir la API de NightBite.
 *
 * Centraliza la configuración de conexión con el backend desarrollado
 * en Spring Boot. Desde aquí se define la URL base y se crea una instancia
 * de ApiService para realizar peticiones HTTP.
 */
object RetrofitClient {

    private const val BASE_URL = "http://192.168.1.12:8080/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}