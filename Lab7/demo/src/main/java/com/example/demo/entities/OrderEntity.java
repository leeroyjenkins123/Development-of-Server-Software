package com.example.demo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = true)
    private CustomerEntity customer;

    @NotNull
    @Column(name = "date", nullable = false)
    private Timestamp date;

    @NotNull
    @Column(name = "status", nullable = false)
    private String status;
}
