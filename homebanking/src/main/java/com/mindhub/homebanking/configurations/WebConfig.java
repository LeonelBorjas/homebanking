package com.mindhub.homebanking.configurations;

import com.mindhub.homebanking.filters.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
@Configuration
public class WebConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{ //Crea la seguridad por nosotros

        httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource)) // recibimos la configuracion de cors que hicimos previamente
                .csrf(AbstractHttpConfigurer::disable) //cros site request forshare impide ataques de formulario, genera un token unico a cada formulario que tenemos en nuestra aplicacion
                .httpBasic(AbstractHttpConfigurer::disable) // Autenticacion basica que nos provee Spring Security pero no es segura por que las credenciales viajan sin cifrar
                .formLogin(AbstractHttpConfigurer::disable) //no usamos formulario de login

                .headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer.frameOptions(
                        HeadersConfigurer.FrameOptionsConfig::disable)) // Se desabilita para poder consumir apis de terceros en este caso el h2Console que puede intentar cargarse como si fuera un iframe

                .authorizeHttpRequests(authorize -> // request  se encarga de especificar el método http autorizar y su ruta
                        authorize
                                .requestMatchers("/api/auth/login","/api/auth/register","/h2-console/**").permitAll()
                                .requestMatchers("/api/accounts").hasRole("CLIENT")
                                .requestMatchers("/api/clients/current/accounts").hasRole("CLIENT")
                                .requestMatchers("/api/clients/current/create-card").hasRole("CLIENT")
                                .requestMatchers("/api/loans", "/api/loan/application").hasRole("CLIENT")
                                .requestMatchers("/api/clients/**").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )

                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class) // agregamos el jwtRequestFilter antes del filter que tendriamos por defecto
                .sessionManagement(session ->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) //Desabilitamos el uso de sesiones, por que lo manejaremos con autencicacion atravez de token
                ); //STATELESS POR QUE NO LA UTILIZAREMOS
        return httpSecurity.build(); // Construimos la configuracion de la cadena de seguridad
    }
    @Bean
    public PasswordEncoder passwordEncoder(){ // sirve para encritar las contraseñas
        return  new BCryptPasswordEncoder();
    }

    @Bean//autenticamos a los usurios una vez logeados
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

}
