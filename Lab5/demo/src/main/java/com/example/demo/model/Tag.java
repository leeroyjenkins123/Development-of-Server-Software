package com.example.demo.model;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="name",nullable = false, unique = true)
    private String name;

    public Tag(){

    }

    public Tag(Long id, String name){
        this.id = id;
        this.name = name;
    }
}