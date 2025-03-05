package com.example.demo.service;

import com.example.demo.model.Category;
import com.example.demo.model.Pet;
import com.example.demo.model.Status;
import com.example.demo.repository.PetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private PetService petService;

    @Test
    void createPet_Success() {
        Pet pet = new Pet(1L, "Buddy", new Category(), List.of(), Status.AVAILABLE);
        when(petRepository.savePet(any(Pet.class))).thenReturn(pet);

        Pet result = petService.create(pet);
        assertEquals(pet, result);
        verify(petRepository, times(1)).savePet(pet);
    }

    @Test
    void findById_PetExists() {
        Pet pet = new Pet(1L, "Buddy", new Category(), List.of(), Status.AVAILABLE);
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));

        Pet result = petService.findById(1L);
        assertEquals(pet, result);
    }

    @Test
    void findById_PetNotFound() {
        when(petRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> petService.findById(1L));
    }

    @Test
    void deletePet_Success() {
        Pet pet = new Pet(1L, "Buddy", new Category(), List.of(), Status.AVAILABLE);
        when(petRepository.deletePet(1L)).thenReturn(pet);

        Pet result = petService.deletePet(1L);
        assertEquals(pet, result);
    }

    @Test
    void updateById_Success() {
        Pet existingPet = new Pet(1L, "Buddy", new Category(), List.of(), Status.AVAILABLE);
        when(petRepository.findById(1L)).thenReturn(Optional.of(existingPet));
        when(petRepository.savePet(any(Pet.class))).thenReturn(existingPet);

        Pet updatedPet = petService.updateById(1L, "Max", Status.SOLD);
        assertEquals("Max", updatedPet.getName());
        assertEquals(Status.SOLD, updatedPet.getStatus());
    }
}