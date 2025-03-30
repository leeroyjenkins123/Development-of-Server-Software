package com.example.demo.model;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "contract")
public class Contract {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer Id;

    @Column(name = "amount", nullable = false)
    private Integer Amount;

    @ManyToOne
    @JoinColumn(name="client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name="supplier_id")
    private Supplier supplier;
}
