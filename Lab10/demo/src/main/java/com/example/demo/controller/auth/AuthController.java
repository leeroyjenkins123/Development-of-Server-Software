package com.example.demo.controller.auth;

import com.example.demo.dto.auth.AuthRequest;
import com.example.demo.dto.auth.AuthResponse;
import com.example.demo.dto.auth.RegisterRequest;
import com.example.demo.dto.auth.UpdateCredentialsRequest;
import com.example.demo.entity.user.UserEntity;
import com.example.demo.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication and management")
@SecurityRequirement(name = "bearerAuth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        return authService.register(
                request.getUsername(),
                request.getPassword(),
                request.getCustomerId()
        );
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest request) {
        ResponseEntity<String> response = authService.login(
                request.getUsername(),
                request.getPassword()
        );
        return response.getBody();
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

    @Operation(summary = "Get current user info")
    @GetMapping("/me")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<JwtAuthenticationToken> getCurrentUser(JwtAuthenticationToken authentication) {
        return ResponseEntity.ok(authentication);
    }
}
