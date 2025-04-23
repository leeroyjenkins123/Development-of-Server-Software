package com.example.demo.dto.auth;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private Long customerId; // Добавляем поле для customer_id

    public RegisterRequest(){

    }

    public RegisterRequest(String username, String password, Long customerId){
        this.username = username;
        this.password = password;
        this.customerId = customerId;
    }
}