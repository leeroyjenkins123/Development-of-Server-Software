package com.example.demo.entity.item;

import com.example.demo.entity.measurement.WeightEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Entity
@Table(name = "items")
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "shipping_weight", referencedColumnName = "id", nullable = true)
    private WeightEntity shippingWeight;
}
