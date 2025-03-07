package com.example.demo.controllers;

import com.example.demo.models.Person;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetExistingPerson() throws Exception {
        mockMvc.perform(get("/persons/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John"));
    }

    @Test
    public void testGetNonExistingPerson() throws Exception {
        mockMvc.perform(get("/persons/100"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Person with id 100 not found"));
    }

    @Test
    public void testPostNewPersons() throws Exception {
        List<Person> newPersons = Arrays.asList(
                new Person(4L, "Alice Smith", 28L),
                new Person(5L, "Bob Johnson", 35L)
        );

        String jsonContent = objectMapper.writeValueAsString(newPersons);

        mockMvc.perform(post("/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[?(@.id == 4)].name").value("Alice Smith"))
                .andExpect(jsonPath("$.length()").value(5));
    }
}
