package com.example.demo.entity.order;

import com.example.demo.entity.item.ItemEntity;
import com.example.demo.entity.measurement.QuantityEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Entity
@Table(name = "order_details")
public class OrderDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false)
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id", nullable = false)
    private ItemEntity item;

    @ManyToOne
    @JoinColumn(name = "quantity_id", referencedColumnName = "id", nullable = false)
    private QuantityEntity quantity;

    @NotNull
    @Column(name = "tax_status", nullable = false)
    private String taxStatus;
}
