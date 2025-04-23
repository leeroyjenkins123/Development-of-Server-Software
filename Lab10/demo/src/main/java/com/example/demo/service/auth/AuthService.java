package com.example.demo.service.auth;

import com.example.demo.dto.auth.AuthResponse;
import com.example.demo.entity.customer.CustomerEntity;
import com.example.demo.entity.user.*;
import com.example.demo.repository.user.RoleRepository;
import com.example.demo.repository.user.TokenRepository;
import com.example.demo.repository.user.UserRepository;
import com.example.demo.exception.GlobalExceptionHandler;
import com.example.demo.repository.customer.CustomerRepository;
import com.example.demo.service.user.CustomUserDetailsService;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;
    @Autowired
    private Keycloak keycloak;
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.client-id}")
    private String clientId;
    @Value("${resource-url")
    private String tokenUri;
    @Value("${resource-url}")
    private String resourceServerUrl;
    @Value("${grant-type}")
    private String grantType;
//
//    private final Keycloak keycloak;
//    private final RestTemplate restTemplate;
//
//    public AuthService(Keycloak keycloak, RestTemplate restTemplate) {
//        this.keycloak = keycloak;
//        this.restTemplate = restTemplate;
//    }

    public String register(String username, String password,Long customerId) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail("dummy@" + username + ".com");
        user.setFirstName("Default");
        user.setLastName("User");
        user.setEnabled(true);
        user.setCredentials(Collections.singletonList(createPasswordCredentials(password)));

        UsersResource usersResource = keycloak.realm(realm).users();
        Response response = usersResource.create(user);

        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed to create user in Keycloak");
        }

        // 2. Назначаем роль пользователю
        String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
        usersResource.get(userId).roles().realmLevel()
                .add(Collections.singletonList(keycloak.realm(realm).roles().get("ROLE_USER").toRepresentation()));
        RoleEntity role = roleRepository.findByRole("ROLE_USER"); // Всегда USER по умолчанию
//                .orElseThrow(() -> new RuntimeException("Default role ROLE_USER not found"));

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(passwordEncoder.encode(password));
        userEntity.setRole(role);
        if (customerId != null) {
            CustomerEntity customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            userEntity.setCustomer(customer);
        }

        userRepository.save(userEntity);

        return "User registered successfully";
    }

    @Transactional
    public String changeUserRole(Long userId, Long newRoleId) {
        UserEntity currentUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!currentUser.getRole().getRole().equals("ROLE_ADMIN")) {
            throw new AccessDeniedException("Only admin can change roles");
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        RoleEntity role = roleRepository.findById(newRoleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.setRole(role);
        userRepository.save(user);
        return "User role updated successfully";
    }

    public ResponseEntity<String> login(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "client_id=" + clientId +
                "&username=" + username +
                "&password=" + password +
                "&grant_type=password";

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        var restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(
                resourceServerUrl,
                HttpMethod.POST,
                request,
                String.class
        );

        AuthResponse authResponse = new AuthResponse();
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {

            authResponse.setToken((String) response.getBody());

        }
        return response;
    }

    @Transactional
    public String updateUserCredentials(Long userId, String newUsername, String newPassword) {
        UserEntity currentUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Проверяем, что пользователь меняет свои данные или это админ
        if (!currentUser.getId().equals(userId) &&
                !currentUser.getRole().getRole().equals("ROLE_ADMIN")) {
            throw new AccessDeniedException("You can only update your own credentials");
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (newUsername != null && !newUsername.isBlank()) {
            if (userRepository.existsByUsername(newUsername)) {
                throw new RuntimeException("Username already exists");
            }
            user.setUsername(newUsername);
        }

        if (newPassword != null && !newPassword.isBlank()) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }

        userRepository.save(user);
        return "User credentials updated successfully";
    }

    @Transactional
    public String changeUserCustomer(Long userId, Long newCustomerId) {
        UserEntity currentUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!currentUser.getRole().getRole().equals("ROLE_ADMIN")) {
            throw new AccessDeniedException("Only admin can change customer ID");
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        CustomerEntity customer = customerRepository.findById(newCustomerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        user.setCustomer(customer);
        userRepository.save(user);

        return "User's customer ID updated successfully";
    }

    private CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }

}
