package com.bfhl.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the /bfhl controller endpoint.
 */
@SpringBootTest
@AutoConfigureMockMvc
class BfhlControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /bfhl returns 200 with is_success true for valid input")
    void testValidRequest() throws Exception {
        String body = objectMapper.writeValueAsString(
                Map.of("data", List.of("a", "1", "334", "4", "R", "$")));

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.is_success").value(true))
                .andExpect(jsonPath("$.user_id").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.roll_number").exists())
                .andExpect(jsonPath("$.odd_numbers[0]").value("1"))
                .andExpect(jsonPath("$.even_numbers[0]").value("334"))
                .andExpect(jsonPath("$.alphabets[0]").value("A"))
                .andExpect(jsonPath("$.special_characters[0]").value("$"))
                .andExpect(jsonPath("$.sum").value("339"))
                .andExpect(jsonPath("$.concat_string").value("Ra"));
    }

    @Test
    @DisplayName("POST /bfhl returns 400 when data field is missing")
    void testMissingDataField() throws Exception {
        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.is_success").value(false))
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    @DisplayName("POST /bfhl returns 400 for malformed JSON")
    void testMalformedJson() throws Exception {
        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("not-json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.is_success").value(false));
    }

    @Test
    @DisplayName("POST /bfhl Example C - alphabets only")
    void testExampleC() throws Exception {
        String body = objectMapper.writeValueAsString(
                Map.of("data", List.of("A", "ABCD", "DOE")));

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.odd_numbers").isEmpty())
                .andExpect(jsonPath("$.even_numbers").isEmpty())
                .andExpect(jsonPath("$.sum").value("0"))
                .andExpect(jsonPath("$.concat_string").value("EoDdCbAa"));
    }
}
