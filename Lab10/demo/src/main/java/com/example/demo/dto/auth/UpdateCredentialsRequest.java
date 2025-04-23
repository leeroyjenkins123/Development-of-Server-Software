package com.example.demo.dto.auth;

import lombok.*;

@Data
public class UpdateCredentialsRequest {
    private String newUsername;
    private String newPassword;

    public UpdateCredentialsRequest(){

    }

    public UpdateCredentialsRequest(String newUsername, String newPassword){
        this.newPassword = newPassword;
        this.newUsername = newUsername;
    }
}
