package com.learning.springBootLearn.controllers;

import com.learning.springBootLearn.dto.LoginUserDto;
import com.learning.springBootLearn.dto.RegisterUserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest                  // levanta el contexto completo
@AutoConfigureMockMvc            // configura MockMvc sin servidor real
@ActiveProfiles("test")          // usa application-test.properties (H2, etc.)
class AuthenticationE2ESpec {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void signup_withValidData_returns200AndUser() throws Exception {
        RegisterUserDto dto = new RegisterUserDto("jane@example.com","secret123","Jane Doe");

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("jane@example.com"))
                .andExpect(jsonPath("$.password").doesNotExist()); // nunca exponer la password
    }

    @Test
    void signup_withDuplicateEmail_returns409() throws Exception {
        RegisterUserDto dto = new RegisterUserDto("duplicate@example.com", "secret123","Jane Doe");

        // primer registro
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        // segundo registro con el mismo email → debe fallar
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict()); // ajustá según tu manejo de errores
    }

    // ─── /login ─────────────────────────────────────────────────

    @Test
    void login_withValidCredentials_returnsTokenAndExpiry() throws Exception {
        // 1. registrar primero
        RegisterUserDto registerDto = new RegisterUserDto("john@example.com","secret123","John Doe");

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk());

        // 2. login
        LoginUserDto loginDto = new LoginUserDto();
        loginDto.setEmail("john@example.com");
        loginDto.setPassword("secret123");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.expiresIn").isNumber());
    }

    @Test
    void login_withWrongPassword_returns401() throws Exception {
        LoginUserDto loginDto = new LoginUserDto();
        loginDto.setEmail("noexiste@example.com");
        loginDto.setPassword("wrongpass");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isUnauthorized());
    }
}