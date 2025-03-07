package com.example.demo.models;

import lombok.Data;

@Data
public class Person {
    private Long id;
    private String name;
    private Long age;

    public Person(Long id, String name, Long age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
}
