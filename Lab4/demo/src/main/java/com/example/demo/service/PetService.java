package com.example.demo.service;

import com.example.demo.model.Pet;
import com.example.demo.model.Status;
import com.example.demo.repository.PetRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetService {

    private final PetRepository petRepository;

    @Autowired
    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public Pet create(Pet pet){
        return petRepository.savePet(pet);
    }

    public Pet findById(Long id){
        return petRepository.findById(id).orElseThrow(() -> new RuntimeException("Pet not found with id: " + id));
    }

    public List<Pet> findAllPets(){
        return petRepository.findAll();
    }

    public Pet deletePet(Long id){
        return petRepository.deletePet(id);
    }

    public Pet updateById(Long id, String name, Status status){
        Pet pet = findById(id);
        if (pet.getId() == null) {
            throw new IllegalArgumentException("Pet not found");
        }
        if (name != null) pet.setName(name);
        if (status != null) pet.setStatus(status);
        return petRepository.savePet(pet);
    }

    public Pet update(Pet pet){
        if (pet.getId() == null || petRepository.findById(pet.getId()).isEmpty()) {
            throw new IllegalArgumentException("Pet not found");
        }
        return petRepository.savePet(pet);
    }
}
