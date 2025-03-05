package com.example.demo.model;

import lombok.Data;
import java.util.List;

@Data
public class Pet {
    private Long id;
    private String name;
    private Category category;
    private List<Tag> tags;
    private Status status;

    public Pet() {
    }

    public Pet(Long id, String name, Category category, List<Tag> tags, Status status) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.tags = tags;
        this.status = status;
    }
}
