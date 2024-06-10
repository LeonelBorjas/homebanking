package com.mindhub.homebanking.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindhub.homebanking.dtos.requestBody.CardsDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthenticationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAuthentication() throws Exception {
        String email = "goku@hotmai.com";
        String password = "password123";

        // Registrarse primero
        String registrationRequest = "{\"firstName\": \"Goku\", \"lastName\": \"Sayayin\", \"email\": \"" + email + "\", \"password\": \"" + password + "\"}";
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrationRequest))
                .andExpect(status().isCreated());

        // Iniciar sesión con las credenciales registradas
        String loginRequest = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}";
        ResultActions result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest));
        result.andExpect(status().isOk());

        // solicitud get al prestamo de un cliente
        mockMvc.perform(get("/api/loans")
                        .with(user("goku@hotmai.com").password("password123").roles("CLIENT")))
                .andExpect(status().isOk());

    }

    @Test
    public void testLoginWithInvalidCredentials() throws Exception {
        // Intento de inicio de sesión con credenciales inválidas
        String loginRequest2 = "{\"email\": \"john@example.com\", \"password\": \"wrongpassword\"}";
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest2))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email or password invalid"));

    }
}

