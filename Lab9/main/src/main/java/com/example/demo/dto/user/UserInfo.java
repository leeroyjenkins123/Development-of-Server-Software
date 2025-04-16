package com.example.demo.dto.user;

import lombok.Data;

@Data
public class UserInfo{
    private Long id;
    private String username;
    private String role;

    // Конструкторы, геттеры и сеттеры
    public UserInfo() {}

    public UserInfo(Long id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }
}
