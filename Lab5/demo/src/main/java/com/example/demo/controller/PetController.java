package com.example.demo.controller;

import com.example.demo.model.Pet;
import com.example.demo.model.Status;
import com.example.demo.service.PetService;
import com.example.demo.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lab5")
public class PetController {
    private final PetService petService;
    private final Validation validation;

    @Autowired
    public PetController(PetService petService, Validation validation) {
        this.petService = petService;
        this.validation = validation;
    }

    @PostMapping("/pet")
    public ResponseEntity<?> addPet(@RequestBody Pet pet) {
        String validationResult = validation.validatePet(pet);
        if (!"Success".equals(validationResult)) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Invalid input: " + validationResult);
        }

        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(petService.create(pet));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating pet: " + e.getMessage());
        }
    }

    @PutMapping("/pet")
    public ResponseEntity<?> updatePet(@RequestBody Pet pet) {
        String validationResult = validation.validatePet(pet);
        if (!"Success".equals(validationResult)) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Invalid input: " + validationResult);
        }

        try {
            Pet updatedPet = petService.update(pet);
            return ResponseEntity.ok(updatedPet);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pet not found");
        }
    }

    @GetMapping("/pet/{petId}")
    public ResponseEntity<?> getPetById(@PathVariable Long petId) {
        try {
            Pet pet = petService.findById(petId);
            return ResponseEntity.ok(pet);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pet not found");
        }
    }

    @GetMapping("/pet")
    public ResponseEntity<List<Pet>> getAllPets() {
        return ResponseEntity.ok(petService.findAllPets());
    }

    @DeleteMapping("/pet/{petId}")
    public ResponseEntity<?> deletePet(@PathVariable Long petId) {
        try {
            petService.deletePet(petId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pet not found");
        }
    }

    @PostMapping("/pet/{petId}")
    public ResponseEntity<?> updatePetWithForm(
            @PathVariable Long petId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Status status
    ) {
        try {
            Pet updatedPet = petService.updateById(petId, name, status);
            return ResponseEntity.ok(updatedPet);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid input: " + e.getMessage());
        }
    }
}