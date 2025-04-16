package com.example.demo.repository.payment;

import com.example.demo.entity.payment.CashEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashRepository extends JpaRepository<CashEntity, Long> {
}
