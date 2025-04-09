package com.example.demo.repository.measurement;

import com.example.demo.entity.measurement.QuantityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuantityRepository extends JpaRepository<QuantityEntity, Long> {
}
