package com.example.demo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "cashes")
public class CashEntity extends PaymentEntity{
    @NotNull
    @Column(name = "cash_tendered", nullable = false)
    private Float cashTendered;
}
