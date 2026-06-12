package ni.edu.uam.nightbiteapp.data.remote

import ni.edu.uam.nightbiteapp.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Cliente de Retrofit para consumir la API de NightBite.
 *
 * La URL base se obtiene desde BuildConfig para separar
 * ambiente local de desarrollo y ambiente de producción.
 */
object RetrofitClient {

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}