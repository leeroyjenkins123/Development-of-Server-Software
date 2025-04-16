package com.example.demo.repository.measurement;

import com.example.demo.entity.measurement.WeightEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeightRepository extends JpaRepository<WeightEntity, Long> {
}