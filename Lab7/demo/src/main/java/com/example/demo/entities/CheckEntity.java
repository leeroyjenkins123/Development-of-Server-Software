package com.example.demo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "checks")
public class CheckEntity extends PaymentEntity{
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "bank_id", nullable = false)
    private String bankId;
}
