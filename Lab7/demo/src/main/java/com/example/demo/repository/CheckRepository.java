package com.example.demo.repository;

import com.example.demo.entities.CheckEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckRepository extends JpaRepository<CheckEntity, Long> {
}
