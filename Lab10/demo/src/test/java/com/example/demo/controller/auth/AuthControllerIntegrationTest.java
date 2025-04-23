package com.example.demo.controller.auth;

import com.example.demo.AbstractIntegrationTest;
import com.example.demo.dto.auth.AuthRequest;
import com.example.demo.dto.auth.AuthResponse;
import com.example.demo.dto.auth.UpdateCredentialsRequest;
import com.example.demo.entity.customer.CustomerEntity;
import com.example.demo.entity.user.RoleEntity;
import com.example.demo.entity.user.UserEntity;
import com.example.demo.repository.customer.CustomerRepository;
import com.example.demo.repository.user.RoleRepository;
import com.example.demo.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class AuthControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String adminToken;
    private String userToken;

    @BeforeEach
    void setUp() {
        // Clear and setup test data
        userRepository.deleteAll();

        // Create roles
        RoleEntity adminRole = roleRepository.findByRole("ROLE_ADMIN");

        RoleEntity userRole = roleRepository.findByRole("ROLE_USER");

        List<CustomerEntity> customer = customerRepository.findAll();

        // Create admin user
        UserEntity admin = new UserEntity();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("adminpass"));
        admin.setRole(adminRole);
        admin.setCustomer(customer.getFirst());
        userRepository.save(admin);

        // Create regular user
        UserEntity user = new UserEntity();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("userpass"));
        user.setRole(userRole);
        user.setCustomer(customer.getFirst());
        userRepository.save(user);

        // Get tokens for testing
        adminToken = loginAndGetToken("admin", "adminpass");
        userToken = loginAndGetToken("user", "userpass");
    }

    private String loginAndGetToken(String username, String password) {
        AuthRequest request = new AuthRequest();
        request.setUsername(username);
        request.setPassword(password);

        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
                "/api/auth/login",
                request,
                AuthResponse.class
        );

        return response.getBody().getToken();
    }

    private HttpEntity<Object> createRequestEntity(Object body, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (token != null) {
            headers.setBearerAuth(token);
        }
        return new HttpEntity<>(body, headers);
    }

    @Test
    void register_ShouldReturnSuccessMessage() {
        AuthRequest request = new AuthRequest();
        request.setUsername("newuser");
        request.setPassword("newpass");
        request.setCustomerId(1L);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/auth/register",
                request,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("successfully"));
    }

    @Test
    void login_WithValidCredentials_ShouldReturnToken() {
        AuthRequest request = new AuthRequest();
        request.setUsername("user");
        request.setPassword("userpass");

        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
                "/api/auth/login",
                request,
                AuthResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getToken());
    }

    @Test
    void login_WithInvalidCredentials_ShouldReturnUnauthorized() {
        AuthRequest request = new AuthRequest();
        request.setUsername("user");
        request.setPassword("wrongpass");

        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
                "/api/auth/login",
                request,
                AuthResponse.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateCredentials_AsAuthenticatedUser_ShouldUpdateCredentials() {
        UpdateCredentialsRequest request = new UpdateCredentialsRequest();
        request.setNewUsername("updateduser");
        request.setNewPassword("updatedpass");

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/auth/update-credentials",
                HttpMethod.PUT,
                createRequestEntity(request, userToken),
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("successfully"));

        // Verify the update by logging in with new credentials
        AuthRequest loginRequest = new AuthRequest();
        loginRequest.setUsername("updateduser");
        loginRequest.setPassword("updatedpass");

        ResponseEntity<AuthResponse> loginResponse = restTemplate.postForEntity(
                "/api/auth/login",
                loginRequest,
                AuthResponse.class
        );

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
    }

    @Test
    void changeUserRole_AsAdmin_ShouldChangeRole() {
        // Get a user to change role (the regular user created in setup)
        UserEntity user = userRepository.findByUsername("user").orElseThrow();
        Long newRoleId = roleRepository.findByRole("ROLE_ADMIN").getId();

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/auth/change-role/" + user.getId() + "?newRole=" + newRoleId,
                HttpMethod.POST,
                createRequestEntity(null, adminToken),
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("successfully"));
    }

    @Test
    void changeUserRole_AsNonAdmin_ShouldReturnForbidden() {
        UserEntity user = userRepository.findByUsername("user").orElseThrow();
        Long newRoleId = roleRepository.findByRole("ROLE_ADMIN").getId();

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/auth/change-role/" + user.getId() + "?newRole=" + newRoleId,
                HttpMethod.POST,
                createRequestEntity(null, userToken),
                String.class
        );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void changeCustomer_AsAdmin_ShouldChangeCustomer() {
        UserEntity user = userRepository.findByUsername("user").orElseThrow();
        Long newCustomerId = 2L;

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/auth/change-customer/" + user.getId() + "?newCustomerId=" + newCustomerId,
                HttpMethod.POST,
                createRequestEntity(null, adminToken),
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("successfully"));
    }
}