package com.example.demo.entity.user;

import com.example.demo.entity.customer.CustomerEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name="username",nullable = false, unique = true)
    private String username;

    @NotNull
    @Column(name="password",nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private RoleEntity role;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer; // Связь с CustomerEntity для обычных пользователей
}
