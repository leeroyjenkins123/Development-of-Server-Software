package com.example.demo.repository;

import com.example.demo.model.Category;
import com.example.demo.model.Pet;
import com.example.demo.model.Status;
import com.example.demo.model.Tag;
import com.example.demo.service.PetService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Transactional
public class PetRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("lab5")
            .withUsername("pxlxta")
            .withPassword("12345");

    @BeforeAll
    static void startContainer() {
        postgres.start();
        System.out.println("Postgres URL: " + postgres.getJdbcUrl());
    }

    @AfterAll
    static void stopContainer() {
        if (postgres != null) {
            postgres.stop();
        }
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private PetService petService;

    private static Pet testPet;

    @BeforeAll
    static void setup(@Autowired PetRepository petRepository, @Autowired PetService petService) {
        Category category = petService.getOrCreateCategory("Dogs");
        Tag tag = petService.getOrCreateTag("Friendly");

        testPet = new Pet();
        testPet.setName("Rex");
        testPet.setCategory(category);
        testPet.setTags(List.of(tag));
        testPet.setStatus(Status.AVAILABLE);

        petRepository.save(testPet);
    }

    @Test
    void shouldSaveAndRetrievePet() {
        Optional<Pet> foundPet = petRepository.findById(testPet.getId());

        assertThat(foundPet).isPresent();
        assertThat(foundPet.get().getName()).isEqualTo("Rex");
        assertThat(foundPet.get().getCategory().getName()).isEqualTo("Dogs");
    }

    @Test
    void shouldUpdatePet() {
        Pet savedPet = testPet;

        savedPet.setName("Max");
        petRepository.save(savedPet);
        Optional<Pet> updatedPet = petRepository.findById(savedPet.getId());

        assertThat(updatedPet).isPresent();
        assertThat(updatedPet.get().getName()).isEqualTo("Max");
    }

    @Test
    void shouldDeletePet() {
        Pet savedPet = testPet;

        petRepository.deleteById(savedPet.getId());
        Optional<Pet> deletedPet = petRepository.findById(savedPet.getId());

        assertThat(deletedPet).isEmpty();
    }

    @Test
    void shouldFindAllPets() {
        Category category = petService.getOrCreateCategory("Cats");
        Tag tag = petService.getOrCreateTag("Funny");
        Pet anotherPet = new Pet();
        anotherPet.setName("Buddy");
        anotherPet.setCategory(category);
        anotherPet.setTags(List.of(tag));
        anotherPet.setStatus(Status.PENDING);
        petRepository.save(anotherPet);

        List<Pet> allPets = petRepository.findAll();

        assertThat(allPets).hasSize(2);
    }

    @Test
    void shouldHandleRelationshipsCorrectly() {
        Pet savedPet = testPet;
        Category petCategory = savedPet.getCategory();
        List<Tag> petTags = savedPet.getTags();

        assertThat(petCategory).isNotNull();
        assertThat(petCategory.getName()).isEqualTo("Dogs");
        assertThat(petTags).hasSize(1);
        assertThat(petTags.getFirst().getName()).isEqualTo("Friendly");
    }
}