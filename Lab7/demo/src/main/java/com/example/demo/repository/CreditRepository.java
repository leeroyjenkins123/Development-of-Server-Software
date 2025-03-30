package com.example.demo.repository;

import com.example.demo.entities.CreditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditRepository extends JpaRepository<CreditEntity, Long>{
}
