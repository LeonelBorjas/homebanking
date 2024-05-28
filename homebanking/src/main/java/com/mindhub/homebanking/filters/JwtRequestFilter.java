package com.mindhub.homebanking.filters;

import com.mindhub.homebanking.servicesSecurity.JwtUtilService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtUtilService jwtUtilService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            final String authorizationHeader = request.getHeader("Authorization"); //obtenemos el header que seria el emcabezado de autorizacion de la peticion
            String userName = null;
            String jwt = null;

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);
                userName = jwtUtilService.extractUserName(jwt); //extraemos el nombre del token JWT
            }

            if (userName != null && SecurityContextHolder.getContext().getAuthentication() ==  null) { // si el contexto de seguirdad no tiene una establecida
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName); // para cargar los detalles del usuario con el nombre de usuario extraido del token
                if (!jwtUtilService.isTokenExpired(jwt)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    // las credenciales son null por que vamos a trabajar en la autenticacion atravez del token
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // vamos a crear y establecer los detalles de autenticacion basados en la la peticion proporcinada
                    SecurityContextHolder.getContext().setAuthentication(authentication); // vamos a establecer la autenticacion del usuario actual, sirve para gestionar la autenticacion y la autorizacion de los usuarios
                }
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        finally {
            filterChain.doFilter(request, response); //usamos el filter que pusimos como parametro para aplicar el metodo, para que coninue con la siguiente cadena de filtros, pasandole la peticion y la respuesta asociada
        }
    }
}
