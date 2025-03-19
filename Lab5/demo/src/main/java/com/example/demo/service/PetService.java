package com.example.demo.service;

import com.example.demo.model.Pet;
import com.example.demo.model.Category;
import com.example.demo.model.Tag;
import com.example.demo.model.Status;
import com.example.demo.repository.PetRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetService {
    private final PetRepository petRepository;
    @PersistenceContext
    private final EntityManager entityManager;

    public PetService(PetRepository petRepository, EntityManager entityManager) {
        this.petRepository = petRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    public Pet create(Pet pet) {
        Category category = getOrCreateCategory(pet.getCategory().getName());
        pet.setCategory(category);

        // Обработка тегов
        List<Tag> tags = pet.getTags().stream()
                .map(tag -> getOrCreateTag(tag.getName()))
                .collect(Collectors.toList());
        pet.setTags(tags);
        return petRepository.save(pet);
    }

    public Pet findById(Long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pet not found"));
    }

    public List<Pet> findAllPets() {
        return petRepository.findAll();
    }

    @Transactional
    public void deletePet(Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pet not found")); // Проверка существования
        petRepository.delete(pet);
    }

    @Transactional
    public Pet updateById(Long id, String name, Status status) {
        Pet pet = findById(id);
        if (name != null) pet.setName(name);
        if (status != null) pet.setStatus(status);
        return petRepository.save(pet);
    }

    @Transactional
    public Pet update(Pet pet) {
        return petRepository.save(pet);
    }

    @Transactional
    public Category getOrCreateCategory(String name) {

        try {
            return entityManager
                    .createQuery("SELECT c FROM Category c WHERE c.name = :name", Category.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            Category newCategory = new Category();
            newCategory.setName(name);
            entityManager.persist(newCategory);
            return newCategory;
        }
    }

    @Transactional
    public Tag getOrCreateTag(String name) {
        try {
            return entityManager
                    .createQuery("SELECT t FROM Tag t WHERE t.name = :name", Tag.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            Tag newTag = new Tag();
            newTag.setName(name);
            entityManager.persist(newTag);
            return newTag;
        }
    }
}