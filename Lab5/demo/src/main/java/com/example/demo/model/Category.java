package com.example.demo.model;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="name",nullable = false, unique = true)
    private String name;

    public Category(){

    }

    public Category(Long id, String name){
        this.id = id;
        this.name = name;
    }
}