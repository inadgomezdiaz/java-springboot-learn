package com.learning.springBootLearn.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.springBootLearn.dto.LoginUserDto;
import com.learning.springBootLearn.dto.RegisterUserDto;
import com.learning.springBootLearn.entities.User;
import com.learning.springBootLearn.services.AuthenticationService;
import com.learning.springBootLearn.services.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthenticationControllerSpec {

    @MockBean
    private JwtService jwtService;
    @MockBean
    private AuthenticationService authenticationService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private RegisterUserDto signUpDto;
    private LoginUserDto loginDto;
    private User user;

    @BeforeEach
    void setUp() {
        signUpDto = new RegisterUserDto("email@mail.com", "Password123$", "Joe Doe");
        loginDto = new LoginUserDto();
        loginDto.setEmail("email@mail.com");
        loginDto.setPassword("Password123$");

        user = new User();
        user.setEmail("email@mail.com");
        user.setFullName("Joe Doe");
        user.setPassword("encodedPassword");
    }

    @Test
    void shouldSignUpSuccessfully() throws Exception {
        when(authenticationService.signup(ArgumentMatchers.any(RegisterUserDto.class)))
                .thenReturn(user);

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("email@mail.com"))
                .andExpect(jsonPath("$.fullName").value("Joe Doe"));
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {
        when(authenticationService.authenticate(ArgumentMatchers.any(LoginUserDto.class)))
                .thenReturn(user);
        when(jwtService.generateToken(ArgumentMatchers.any(User.class)))
                .thenReturn("jwt-token-123");
        when(jwtService.getExpirationTime())
                .thenReturn(3600000L);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token-123"))
                .andExpect(jsonPath("$.expiresIn").value(3600000L));
    }

}
