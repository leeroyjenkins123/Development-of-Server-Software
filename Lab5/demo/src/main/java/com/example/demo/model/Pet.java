package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import jakarta.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "pets")
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column(name = "name",nullable = false)
    private String name;

    @ManyToOne // Добавлено каскадирование
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany
    @JoinTable(
            name = "pet_tags",
            joinColumns = @JoinColumn(name = "pet_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags;

    @Enumerated(EnumType.STRING)
    @Column(name="status",nullable = false)
    private Status status;

    public Pet(){

    }

    public Pet(String name, Category category, List<Tag> tags, Status status){
        this.name = name;
        this.category = category;
        this.tags = tags;
        this.status = status;
    }
}