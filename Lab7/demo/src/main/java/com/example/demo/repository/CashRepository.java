package com.example.demo.repository;

import com.example.demo.entities.CashEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashRepository extends JpaRepository<CashEntity, Long> {
}
