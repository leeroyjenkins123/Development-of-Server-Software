package com.example.demo.controller;

import com.example.demo.model.Pet;
import com.example.demo.model.Status;
import com.example.demo.service.PetService;
import com.example.demo.validation.Validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/lab4")
public class PetController {
    private final PetService petService;
    private final Validation validation;

    @Autowired
    public PetController(PetService petService, Validation validation) {
        this.petService = petService;
        this.validation = validation;
    }

    @PostMapping("/pet")
    public ResponseEntity<?> addPet(@RequestBody Pet pet){
        String result = validation.validatePet(pet);

        if(!Objects.equals(result, "Success")){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Invalid input");
        }
        try {
            return ResponseEntity.status(HttpStatus.OK).body(petService.create(pet));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation exception");
        }
    }

    @PutMapping("/pet")
    public ResponseEntity<?> updatePet(@RequestBody Pet pet){
        String result = validation.validatePet(pet);

        if(!Objects.equals(result, "Success")){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Invalid input");
        }

        String resultId = validation.validateID(pet.getId());
        if(!Objects.equals(resultId, "Success")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid ID supplied");
        }

        try {
            return ResponseEntity.status(HttpStatus.OK).body(petService.update(pet));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Pet not found");
        }
    }

    @GetMapping("/pet/{petId}")
    public ResponseEntity<?> getPetById(@PathVariable Long petId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(petService.findById(petId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Pet not found");
        }
    }

    @GetMapping("/pet")
    public ResponseEntity<?> getPets(){
        return ResponseEntity.status(HttpStatus.OK).body(petService.findAllPets());
    }

    @DeleteMapping("/pet/{petId}")
    public ResponseEntity<?> deletePet(@PathVariable Long petId) {
        try {
            return ResponseEntity.ok().body(petService.deletePet(petId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid pet value");
        }
    }

    @PostMapping("/pet/{petId}")
    public ResponseEntity<?> updatePetWithForm(
            @PathVariable Long petId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Status status
    ) {
        try {
            return ResponseEntity.ok().body(petService.updateById(petId, name, status));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid input");
        }
    }
}
