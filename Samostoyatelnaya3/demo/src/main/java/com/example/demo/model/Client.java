package com.example.demo.model;

import lombok.Data;
import jakarta.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer Id;

    @Column(name = "name", nullable = false)
    private String Name;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Contract> Contracts;
}
