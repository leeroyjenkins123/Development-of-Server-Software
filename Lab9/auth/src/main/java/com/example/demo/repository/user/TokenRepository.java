package com.example.demo.repository.user;

import com.example.demo.entity.user.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
    Optional<TokenEntity> findByToken(String tokenValue);

    Optional<TokenEntity> findByUserId(Long id);
}
