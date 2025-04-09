package com.example.demo.controller.order;

import com.example.demo.AbstractIntegrationTest;
import com.example.demo.dto.auth.AuthRequest;
import com.example.demo.dto.auth.AuthResponse;
import com.example.demo.dto.order.OrderResponse;
import com.example.demo.entity.customer.AddressEntity;
import com.example.demo.entity.customer.CustomerEntity;
import com.example.demo.entity.order.OrderEntity;
import com.example.demo.entity.user.RoleEntity;
import com.example.demo.entity.user.UserEntity;
import com.example.demo.repository.customer.AddressRepository;
import com.example.demo.repository.customer.CustomerRepository;
import com.example.demo.repository.order.OrderRepository;
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

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class OrderControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AddressRepository addressRepository;

    private List<CustomerEntity> customerEntities;

    private List<OrderEntity> orderEntities;

    private String adminToken;
    private String userToken;
    private String anotherUserToken;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        RoleEntity adminRole = roleRepository.findByRole("ROLE_ADMIN");

        RoleEntity userRole = roleRepository.findByRole("ROLE_USER");

        List<AddressEntity> addressEntities = addressRepository.findAll();

        customerEntities = customerRepository.findAll();

        // Create admin user
        UserEntity admin = new UserEntity();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("adminpass"));
        admin.setRole(adminRole);
        admin.setCustomer(null); // Admin has no customer
        userRepository.save(admin);

        // Create regular user with customer1
        UserEntity user = new UserEntity();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("userpass"));
        user.setRole(userRole);
        user.setCustomer(customerEntities.getFirst());
        userRepository.save(user);

        // Create another regular user with customer2
        UserEntity anotherUser = new UserEntity();
        anotherUser.setUsername("anotheruser");
        anotherUser.setPassword(passwordEncoder.encode("anotherpass"));
        anotherUser.setRole(userRole);
        anotherUser.setCustomer(customerEntities.get(1));
        userRepository.save(anotherUser);

        orderEntities = orderRepository.findAll();

        adminToken = loginAndGetToken("admin", "adminpass");
        userToken = loginAndGetToken("user", "userpass");
        anotherUserToken = loginAndGetToken("anotheruser", "anotherpass");
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
    void getAllOrders_AsAdmin_ShouldReturnAllOrders() {
        ResponseEntity<List> response = restTemplate.exchange(
                "/api/orders",
                HttpMethod.GET,
                createRequestEntity(null, adminToken),
                List.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().size());
    }

    @Test
    void getAllOrders_AsUser_ShouldReturnOnlyOwnCustomerOrders() {
        ResponseEntity<List> response = restTemplate.exchange(
                "/api/orders",
                HttpMethod.GET,
                createRequestEntity(null, userToken),
                List.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getOrderById_AsAdmin_ShouldReturnAnyOrder() {
        ResponseEntity<OrderResponse> response = restTemplate.exchange(
                "/api/orders/" + orderEntities.getFirst().getId(),
                HttpMethod.GET,
                createRequestEntity(null, adminToken),
                OrderResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(orderEntities.getFirst().getId(), response.getBody().getId());
    }

    @Test
    void getOrderById_AsUser_ShouldReturnOwnCustomerOrder() {
        ResponseEntity<OrderResponse> response = restTemplate.exchange(
                "/api/orders/" + orderEntities.getFirst().getId(),
                HttpMethod.GET,
                createRequestEntity(null, userToken),
                OrderResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(orderEntities.getFirst().getId(), response.getBody().getId());
    }

    @Test
    void getOrderById_AsUser_ShouldDenyAccessToOtherCustomerOrder() {
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/orders/" + orderEntities.get(1).getId(),
                HttpMethod.GET,
                createRequestEntity(null, userToken),
                String.class
        );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void findOrdersByCriteria_AsAdmin_ShouldFilterAllOrders() {
        String url = "/api/orders/find?customerName=Иван Иванов&orderStatus=Новый";

        ResponseEntity<List> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                createRequestEntity(null, adminToken),
                List.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void findOrdersByCriteria_AsUser_ShouldFilterOnlyOwnOrders() {
        OrderEntity order3 = new OrderEntity();
        order3.setDate(Timestamp.from(Instant.now()));
        order3.setStatus("COMPLETED");
        order3.setCustomer(customerEntities.getFirst());
        orderRepository.save(order3);

        String url = "/api/orders/find?orderStatus=COMPLETED";

        ResponseEntity<List> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                createRequestEntity(null, userToken),
                List.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void findOrdersByCriteria_WithDateRange_ShouldFilterCorrectly() {
        Timestamp start = orderEntities.getFirst().getDate();
        Timestamp end = Timestamp.from(Instant.now().plusSeconds(1800));
        String url = String.format("/api/orders/find?startTime=%s&endTime=%s", start, end);

        ResponseEntity<List> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                createRequestEntity(null, adminToken),
                List.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().size());
    }

    @Test
    void findOrdersByCriteria_WithLocation_ShouldFilterCorrectly() {
        String url = "/api/orders/find?city=Новосибирск&street=ул. Достоевского, 3";

        ResponseEntity<List> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                createRequestEntity(null, adminToken),
                List.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        ResponseEntity<List> userResponse = restTemplate.exchange(
                url,
                HttpMethod.GET,
                createRequestEntity(null, userToken),
                List.class
        );
        assertEquals(HttpStatus.OK, userResponse.getStatusCode());
        assertTrue(userResponse.getBody().isEmpty());
    }
}