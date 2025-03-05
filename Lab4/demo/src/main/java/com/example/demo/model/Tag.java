package com.example.demo.model;

import lombok.Data;

@Data
public class Tag {
    private Long id;
    private String name;

    public Tag(){}

    public Tag(Long id, String name){
        this.id = id;
        this.name = name;
    }
}
