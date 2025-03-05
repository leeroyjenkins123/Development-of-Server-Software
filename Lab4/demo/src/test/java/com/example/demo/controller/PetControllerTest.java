package com.example.demo.controller;

import com.example.demo.model.Pet;
import com.example.demo.model.Status;
import com.example.demo.service.PetService;
import com.example.demo.validation.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PetControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PetService petService;

    @Mock
    private Validation validation;

    @InjectMocks
    private PetController petController;
    private Pet pet;

    @BeforeEach
    public void setup() {
        pet = new Pet();
        pet.setId(1L);
        pet.setName("Buddy");
        pet.setStatus(Status.AVAILABLE);
    }

    @Test
    void addPet_ValidPet_ReturnsOk() throws Exception {
        Mockito.when(validation.validatePet(pet)).thenReturn("Success");
        Mockito.when(petService.create(pet)).thenReturn(pet);

        mockMvc = MockMvcBuilders.standaloneSetup(petController).build();

        mockMvc.perform(post("/lab4/pet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Buddy\",\"status\":\"AVAILABLE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Buddy"));
    }

    @Test
    void addPet_InvalidPet_ReturnsUnprocessableEntity() throws Exception {
        Mockito.when(validation.validatePet(Mockito.any())).thenReturn("Invalid input");

        mockMvc = MockMvcBuilders.standaloneSetup(petController).build();

        mockMvc.perform(post("/lab4/pet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("Invalid input"));
    }

    @Test
    void getPetById_ExistingId_ReturnsPet() throws Exception {
        Mockito.when(petService.findById(1L)).thenReturn(pet);

        mockMvc = MockMvcBuilders.standaloneSetup(petController).build();

        mockMvc.perform(get("/lab4/pet/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void updatePetWithForm_ValidData_ReturnsOk() throws Exception {
        Pet updatedPet = new Pet(1L, "Rex", null, null, Status.SOLD);
        Mockito.when(petService.updateById(1L, "Rex", Status.SOLD)).thenReturn(updatedPet);

        mockMvc = MockMvcBuilders.standaloneSetup(petController).build();

        mockMvc.perform(post("/lab4/pet/1")
                        .param("name", "Rex")
                        .param("status", "SOLD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Rex"));
    }

    @Test
    void deletePet_InvalidId_ReturnsBadRequest() throws Exception {
        Mockito.when(petService.deletePet(999L)).thenThrow(new RuntimeException());

        mockMvc = MockMvcBuilders.standaloneSetup(petController).build();

        mockMvc.perform(delete("/lab4/pet/999"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid pet value"));
    }
}