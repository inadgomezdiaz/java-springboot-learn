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
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BudgetE2ESpec {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String PASSWORD = "secret123";

    @Test
    void createBudget_withValidData_returns200AndBudget() throws Exception {
        String token = registerAndGetToken("bob@example.com");
        String body = "{\"limitAmount\": 500, \"month\": \"january\"}";

        mockMvc.perform(post("/budget")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.limitAmount").value(500))
                .andExpect(jsonPath("$.month").value("january"));
    }

    @Test
    void createBudget_withNegativeLimit_returns400() throws Exception {
        String token = registerAndGetToken("carol@example.com");
        String body = "{\"limitAmount\": -100, \"month\": \"january\"}";

        mockMvc.perform(post("/budget")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createBudget_withoutToken_returns403() throws Exception {
        String body = "{\"limitAmount\": 500, \"month\": \"january\"}";

        mockMvc.perform(post("/budget")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isForbidden());
    }

    @Test
    void getBudgets_returnsOnlyOwnBudgets() throws Exception {
        String token = registerAndGetToken("dave@example.com");
        String otherToken = registerAndGetToken("erin@example.com");

        int firstId = createBudget(token, 300, "february");
        int secondId = createBudget(token, 400, "march");
        createBudget(otherToken, 900, "january");

        mockMvc.perform(get("/budget")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[?(@.id == " + firstId + ")]").exists())
                .andExpect(jsonPath("$[?(@.id == " + secondId + ")]").exists())
                .andExpect(jsonPath("$[?(@.limitAmount == 900)]").doesNotExist());
    }

    @Test
    void getBudget_withExistingId_returnsBudget() throws Exception {
        String token = registerAndGetToken("frank@example.com");
        int id = createBudget(token, 250, "april");

        mockMvc.perform(get("/budget/" + id)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.limitAmount").value(250))
                .andExpect(jsonPath("$.month").value("april"));
    }

    @Test
    void getBudget_withNonExistentId_returns404() throws Exception {
        String token = registerAndGetToken("grace@example.com");

        mockMvc.perform(get("/budget/99999")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void getBudget_belongingToAnotherUser_returns404() throws Exception {
        String token = registerAndGetToken("hank@example.com");
        String otherToken = registerAndGetToken("ivy@example.com");
        int id = createBudget(token, 500, "may");

        mockMvc.perform(get("/budget/" + id)
                        .header("Authorization", "Bearer " + otherToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateBudget_withValidData_returns200AndUpdatedBudget() throws Exception {
        String token = registerAndGetToken("jake@example.com");
        int id = createBudget(token, 100, "june");
        String body = "{\"limitAmount\": 800, \"month\": \"july\"}";

        mockMvc.perform(put("/budget/" + id)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.limitAmount").value(800))
                .andExpect(jsonPath("$.month").value("july"));
    }

    @Test
    void updateBudget_belongingToAnotherUser_returns404() throws Exception {
        String token = registerAndGetToken("kevin@example.com");
        String otherToken = registerAndGetToken("laura@example.com");
        int id = createBudget(token, 100, "august");
        String body = "{\"limitAmount\": 999, \"month\": \"september\"}";

        mockMvc.perform(put("/budget/" + id)
                        .header("Authorization", "Bearer " + otherToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteBudget_withOwnBudget_returns204AndRemovesIt() throws Exception {
        String token = registerAndGetToken("mary@example.com");
        int id = createBudget(token, 100, "october");

        mockMvc.perform(delete("/budget/" + id)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/budget/" + id)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteBudget_belongingToAnotherUser_returns404() throws Exception {
        String token = registerAndGetToken("nick@example.com");
        String otherToken = registerAndGetToken("olivia@example.com");
        int id = createBudget(token, 100, "november");

        mockMvc.perform(delete("/budget/" + id)
                        .header("Authorization", "Bearer " + otherToken))
                .andExpect(status().isNotFound());
    }
    private String registerAndGetToken(String email) throws Exception {
        register(email);
        return tokenFor(email);
    }

    private void register(String email) throws Exception {
        RegisterUserDto dto = new RegisterUserDto(email, PASSWORD, "Test User");
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    private String tokenFor(String email) throws Exception {
        LoginUserDto dto = new LoginUserDto();
        dto.setEmail(email);
        dto.setPassword(PASSWORD);

        String response = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(response).get("token").asText();
    }

    private int createBudget(String token, int limitAmount, String month) throws Exception {
        String body = "{\"limitAmount\": " + limitAmount + ", \"month\": \"" + month + "\"}";

        String response = mockMvc.perform(post("/budget")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JsonNode json = objectMapper.readTree(response);
        return json.get("id").asInt();
    }

}
