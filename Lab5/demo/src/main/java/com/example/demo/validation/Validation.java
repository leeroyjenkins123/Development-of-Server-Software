package com.example.demo.validation;

import com.example.demo.model.Pet;
import org.springframework.stereotype.Service;

@Service
public class Validation {
    public String validatePet(Pet pet) {
        if (pet == null) {
            return "Pet cannot be null";
        }
        return "Success";
    }

    public String validateID(Long id){
        if (id < 0) {
            return "ID should be positive integer number";
        }
        return "Success";
    }

    public String validateName(Pet pet) {
        if (pet.getName() == null || pet.getName().trim().isEmpty()) {
            return "Name cannot be empty";
        }
        return "Success";
    }

    public String validateCategory(Pet pet){
        if (pet.getCategory() == null) {
            return "Category cannot be null";
        }
        return "Success";
    }

    public String validateStatus(Pet pet) {
        if (pet.getStatus() == null) {
            return "Status cannot be null";
        }
        return "Success";
    }
}
