package com.mindhub.homebanking.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig { // Cros origin resorcuse sherin

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("*")); //Lista de todos los headers que nos puedan mandar
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); //Fuente de configuraciones para las rutas
        source.registerCorsConfiguration("/**", configuration); // que rutas de nuestra app pueden pegarle
        return source; //retorna esto para que se apliquen estas configuraciones del cors
    }


}
