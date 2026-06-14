package ni.edu.uam.nightbiteapp.data.remote

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import ni.edu.uam.nightbiteapp.data.local.session.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor encargado de agregar el token JWT a las peticiones protegidas.
 *
 * Si existe un token guardado en la sesión local, se agrega el header:
 * Authorization: Bearer <token>
 */
class AuthInterceptor(
    private val sessionManager: SessionManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val path = originalRequest.url.encodedPath

        val isPublicEndpoint =
            path.endsWith("/api/users/login") ||
                    path.endsWith("/api/users/register") ||
                    path.endsWith("/api/health")

        if (isPublicEndpoint) {
            return chain.proceed(originalRequest)
        }

        val token = runBlocking {
            sessionManager.userSessionFlow.first().token
        }

        val authenticatedRequest = if (token.isNotBlank()) {
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }

        return chain.proceed(authenticatedRequest)
    }
}