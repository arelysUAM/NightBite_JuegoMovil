package ni.edu.uam.nightbiteapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración base de seguridad para la API REST de NightBite.
 *
 * Esta configuración prepara el backend para trabajar como API móvil:
 * - Desactiva CSRF porque no se usan formularios web.
 * - Desactiva sesiones de navegador.
 * - Permite registro y login.
 * - Bloquea el endpoint que lista todos los usuarios.
 * - Mantiene temporalmente accesibles algunos endpoints hasta implementar JWT.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)

                .cors(Customizer.withDefaults())

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users/login").permitAll()

                        // Evita exponer la lista completa de usuarios.
                        .requestMatchers(HttpMethod.GET, "/api/users").denyAll()

                        // Temporalmente permitido hasta implementar autenticación con token.
                        .requestMatchers(HttpMethod.GET, "/api/users/{id}").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/users/{id}/username").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/users/{id}/password").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").permitAll()

                        .requestMatchers("/api/players/**").permitAll()

                        .anyRequest().permitAll()
                )

                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable);

        return http.build();
    }
}