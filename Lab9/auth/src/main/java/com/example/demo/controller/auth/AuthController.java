package com.example.demo.controller.auth;

import com.example.demo.dto.auth.AuthRequest;
import com.example.demo.dto.auth.AuthResponse;
import com.example.demo.dto.auth.UpdateCredentialsRequest;
import com.example.demo.dto.user.UserInfo;
import com.example.demo.entity.user.UserEntity;
import com.example.demo.service.auth.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication and management")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @PostMapping("/validate")
    public UserInfo validateToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring(7);
            logger.info("Validating token: {}", token);
            UserInfo isValid = authService.validateToken(token);
            logger.info("Token validation result: {}", isValid);
            return isValid;
        }
        return null;
    }

    @PostMapping("/register")
    public String register(@RequestBody AuthRequest request) {
        return authService.register(
                request.getUsername(),
                request.getPassword(),
                request.getCustomerId()
        );
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        String token = authService.login(request.getUsername(), request.getPassword());
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        return response;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/change-role/{userId}")
    public String changeUserRole(@PathVariable Long userId, @RequestParam Long newRole) {
        return authService.changeUserRole(userId, newRole);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/update-credentials")
    public String updateCredentials(@RequestBody UpdateCredentialsRequest request) {
        UserEntity currentUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return authService.updateUserCredentials(
                currentUser.getId(),
                request.getNewUsername(),
                request.getNewPassword()
        );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/change-customer/{userId}")
    public String changeCustomer(@PathVariable Long userId, @RequestParam Long newCustomerId) {
        return authService.changeUserCustomer(userId, newCustomerId);
    }
}
