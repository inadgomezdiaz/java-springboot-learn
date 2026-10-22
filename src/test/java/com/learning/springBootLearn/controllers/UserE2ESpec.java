package com.learning.springBootLearn.controllers;

import com.learning.springBootLearn.dto.LoginUserDto;
import com.learning.springBootLearn.dto.RegisterUserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserE2ESpec {

    private static final String PASSWORD = "secret123";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAuthenticatedUser_returnsRegisteredUser() throws Exception {
        String token = registerAndGetToken("jane@example.com", "Jane Doe");

        mockMvc.perform(get("/users/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("jane@example.com"))
                .andExpect(jsonPath("$.fullName").value("Jane Doe"));
    }

    @Test
    void getAllUsers_returnsRegisteredUsersInDefaultSortOrder() throws Exception {
        String token = registerAndGetToken("jane@example.com", "Jane Doe");
        register("john@example.com", "John Doe");

        mockMvc.perform(get("/users")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].email").value("john@example.com"))
                .andExpect(jsonPath("$[1].email").value("jane@example.com"));
    }

    @Test
    void getAllUsers_appliesPageableQueryParameters() throws Exception {
        String token = registerAndGetToken("a@example.com", "User A");
        register("b@example.com", "User B");

        mockMvc.perform(get("/users")
                        .header("Authorization", "Bearer " + token)
                        .param("page", "1")
                        .param("size", "1")
                        .param("sort", "email,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].email").value("b@example.com"));
    }

    private String registerAndGetToken(String email, String fullName) throws Exception {
        register(email, fullName);

        LoginUserDto loginDto = new LoginUserDto();
        loginDto.setEmail(email);
        loginDto.setPassword(PASSWORD);

        String response = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode json = objectMapper.readTree(response);
        return json.get("token").asText();
    }

    private void register(String email, String fullName) throws Exception {
        RegisterUserDto registerDto = new RegisterUserDto(email, PASSWORD, fullName);

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk());
    }
}
