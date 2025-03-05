package com.example.demo.repository;

import com.example.demo.model.Pet;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PetRepository {
    private final Map<Long, Pet> storage = new HashMap<>();
    private Long nextId = 1L;

    public Optional<Pet> findById(Long id){
        return Optional.ofNullable(storage.get(id));
    }

    public List<Pet> findAll(){
        return new ArrayList<>(storage.values());
    }

    public Pet savePet(Pet pet){
        if (pet.getId() == null) {
            pet.setId(nextId++);
        }
        storage.put(pet.getId(), pet);
        return pet;
    }

    public Pet deletePet(Long id){
        return storage.remove(id);
    }
}
