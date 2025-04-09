package com.example.demo.service.auth;

import com.example.demo.entity.customer.CustomerEntity;
import com.example.demo.entity.user.*;
import com.example.demo.repository.user.RoleRepository;
import com.example.demo.repository.user.TokenRepository;
import com.example.demo.repository.user.UserRepository;
import com.example.demo.exception.GlobalExceptionHandler;
import com.example.demo.repository.customer.CustomerRepository;
import com.example.demo.service.user.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
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

    public String register(String username, String password,Long customerId) {
        RoleEntity role = roleRepository.findByRole("ROLE_USER"); // Всегда USER по умолчанию
//                .orElseThrow(() -> new RuntimeException("Default role ROLE_USER not found"));

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        if (customerId != null) {
            CustomerEntity customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            user.setCustomer(customer);
        }

        userRepository.save(user);

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

    public String login(String username, String password) {

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Проверка на совпадение пароля
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

//        UserEntity user = userRepository.findByUsername(username).orElseThrow();

        Optional<TokenEntity> activeTokenOpt = tokenRepository.findByUserId(user.getId()).stream()
                .filter(token -> token.getExpiresAt().after(new Timestamp(System.currentTimeMillis())))
                .findFirst();

        if (activeTokenOpt.isPresent()) {
            return activeTokenOpt.get().getToken();
        }

        String token = UUID.randomUUID().toString();

        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setToken(token);
        tokenEntity.setUser(user);
        tokenRepository.save(tokenEntity);

        return token;
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

}
