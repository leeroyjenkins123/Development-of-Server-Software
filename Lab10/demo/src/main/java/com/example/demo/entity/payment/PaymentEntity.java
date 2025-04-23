package com.example.demo.entity.payment;

import com.example.demo.entity.order.OrderEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Entity
@Table(name = "payments")
@Inheritance(strategy = InheritanceType.JOINED)
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false)
    private OrderEntity order;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Float amount;
}
