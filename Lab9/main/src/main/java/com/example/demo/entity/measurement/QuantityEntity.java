package com.example.demo.entity.measurement;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "quantities")
public class QuantityEntity extends MeasurementEntity{
    @NotNull
    @Column(name = "value", nullable = false)
    private Integer value;
}
