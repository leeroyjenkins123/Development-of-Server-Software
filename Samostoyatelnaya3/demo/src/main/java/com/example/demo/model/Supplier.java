package com.example.demo.model;

import lombok.Data;
import jakarta.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "supplier")
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer Id;

    @Column(name = "product", nullable = false)
    private String Product;

    @Column(name = "quantity", nullable = false)
    private Integer Quantity;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL)
    private List<Contract> Contracts;
}
