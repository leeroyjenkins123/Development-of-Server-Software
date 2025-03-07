package com.example.demo.controllers;

import com.example.demo.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/persons")
public class PersonController {
    private final List<Person> persons = new ArrayList<>();
    private final Set<Long> personIds = new HashSet<>();

    @Autowired
    public PersonController() {
        this.persons.add(new Person(1L, "John", 30L));
        this.persons.add(new Person(2L, "Jahn", 25L));
        this.persons.add(new Person(3L, "Jihn", 40L));
    }

    @PostMapping
    public ResponseEntity<?> post(@RequestBody List<Person> newPersons){
        for (Person person : newPersons) {
            if (person.getId() == null || person.getId() < 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("ID must be non-negative and not null");
            }
            if (personIds.contains(person.getId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Person with ID " + person.getId() + " already exists");
            }
        }
        try {
            for(Person person : newPersons){
                persons.add(person);
                personIds.add(person.getId());
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(persons);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error adding persons: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<Person> person = persons.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        if (person.isPresent()) {
            return ResponseEntity.ok(person.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person with id " + id + " not found");
        }
    }
}
