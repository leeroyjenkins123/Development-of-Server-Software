package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
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