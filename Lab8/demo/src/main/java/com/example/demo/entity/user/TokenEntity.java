package com.example.demo.entity.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tokens")
public class TokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @NotNull
    @Column(name="token",nullable = false, unique = true)
    private String token;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name="created_at",nullable = false)
    private Timestamp createdAt;

    @Column(name="expires_at",nullable = false)
    private Timestamp expiresAt;

    @PrePersist
    public void prePersist() {
        createdAt = new Timestamp(System.currentTimeMillis()); // Устанавливаем время создания
        expiresAt = new Timestamp(System.currentTimeMillis() + 10L * 24 * 60 * 60 * 1000); // Устанавливаем время истечения через 10 дней
    }
}
