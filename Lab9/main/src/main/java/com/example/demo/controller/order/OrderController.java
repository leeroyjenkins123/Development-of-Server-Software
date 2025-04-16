package com.example.demo.controller.order;

import com.example.demo.dto.order.OrderResponse;
import com.example.demo.entity.customer.CustomerEntity;
import com.example.demo.entity.order.OrderEntity;
import com.example.demo.entity.user.UserEntity;
import com.example.demo.exception.AccessDeniedException;
import com.example.demo.service.order.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "Endpoints for order management")
@SecurityRequirement(name = "bearerAuth")
public class OrderController{

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderResponse>> getAllOrders(@AuthenticationPrincipal UserEntity user) {
        logger.info("Получен запрос от пользователя: {}", user.getUsername());
        List<OrderEntity> orders = orderService.getAllAccessibleOrders(user);
        if (orders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        List<OrderResponse> dtos = orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(dtos);
    }

    private OrderResponse convertToDTO(OrderEntity order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setDate(order.getDate());
        response.setStatus(order.getStatus());

        if (order.getCustomer() != null) {
            response.setCustomerId(order.getCustomer().getId());
            response.setCustomerName(order.getCustomer().getName());
        }

        return response;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') and @orderService.isCustomerOrder(#orderId, principal))")
    @GetMapping(value = "/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long orderId,
                                                      @AuthenticationPrincipal UserEntity user) throws AccessDeniedException {
        OrderEntity order = orderService.getOrderByIdIfAuthorized(orderId, user);
        OrderResponse response = convertToDTO(order);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping(value = "/find",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderResponse>> findOrdersByCriteria(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String street,
            @RequestParam(required = false) String zipcode,
            @RequestParam(required = false) Timestamp startTime,
            @RequestParam(required = false) Timestamp endTime,
            @RequestParam(required = false) String paymentType,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String taxStatus,
            @RequestParam(required = false) String orderStatus,
            @AuthenticationPrincipal UserEntity user) {

        List<OrderEntity> orders;
        if (!user.getRole().getRole().equals("ROLE_ADMIN")) {
            CustomerEntity customer = user.getCustomer();
            if (customer == null) {
                return ResponseEntity.ok(List.of());
            }
            orders = orderService.getOrdersByCustomer(customer);
        } else {
            orders = orderService.getAllAccessibleOrders(user);
        }

        List<OrderEntity> filteredOrders = orderService.findOrdersByCriteria(
                 orders, city, street, zipcode, startTime, endTime,
                paymentType, customerName, taxStatus, orderStatus);

        List<OrderResponse> dtos = filteredOrders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok().contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)).body(dtos);
    }
}
