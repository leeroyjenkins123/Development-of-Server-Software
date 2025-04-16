package com.example.demo.service.auth;

import com.example.demo.dto.user.UserInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "auth-service", url = "${auth.service.url}")
public interface AuthClient {
    @PostMapping("/api/auth/validate")
    UserInfo validateToken(@RequestHeader("Authorization") String token);
}