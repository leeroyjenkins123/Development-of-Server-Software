package com.example.demo.service.order;

import com.example.demo.entity.customer.*;
import com.example.demo.entity.item.*;
import com.example.demo.entity.measurement.*;
import com.example.demo.entity.order.*;
import com.example.demo.entity.payment.*;
import com.example.demo.entity.user.*;

import com.example.demo.exception.AccessDeniedException;
import com.example.demo.exception.OrderNotFoundException;
import com.example.demo.repository.customer.*;
import com.example.demo.repository.item.*;
import com.example.demo.repository.measurement.*;
import com.example.demo.repository.order.*;
import com.example.demo.repository.payment.*;
import com.example.demo.repository.user.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final PaymentRepository paymentRepository;
    private final CashRepository cashRepository;
    private final CheckRepository checkRepository;
    private final CreditRepository creditRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        OrderDetailRepository orderDetailRepository,
                        CustomerRepository customerRepository,
                        AddressRepository addressRepository,
                        PaymentRepository paymentRepository,
                        CashRepository cashRepository,
                        CheckRepository checkRepository,
                        CreditRepository creditRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
        this.paymentRepository = paymentRepository;
        this.cashRepository = cashRepository;
        this.checkRepository = checkRepository;
        this.creditRepository = creditRepository;
    }

    public boolean isCustomerOrder(Long orderId, UserEntity user) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    CustomerEntity customer = user.getCustomer();
                    return customer != null && order.getCustomer().getId().equals(customer.getId());
                })
                .orElse(false);
    }


    @Transactional(readOnly = true)
    public List<OrderEntity> getAllAccessibleOrders(UserEntity user) {
        log.info("Checking orders for user: {}, role: {}", user.getUsername(), user.getRole().getRole());

        if (user.getRole().getRole().equals("ROLE_ADMIN")) {
            List<OrderEntity> allOrders = orderRepository.findAll();
            log.info("Admin access. Found {} orders", allOrders.size());
            return allOrders;
        } else {
            CustomerEntity customer = user.getCustomer();
            if (customer != null) {
                log.info("User customer ID: {}", customer.getId());
                List<OrderEntity> userOrders = orderRepository.findByCustomerId(customer.getId());
                log.info("Found {} orders for customer", userOrders.size());
                return userOrders;
            } else {
                log.warn("No customer associated with user {}", user.getUsername());
                return List.of();
            }
        }
    }

    public OrderEntity getOrderByIdIfAuthorized(Long orderId, UserEntity user) throws AccessDeniedException {
        Optional<OrderEntity> optional = orderRepository.findById(orderId);
        if (optional.isEmpty()) {
            throw new OrderNotFoundException("ID = " + orderId);
        }

        OrderEntity order = optional.get();
        if (user.getRole().getRole().equals("ROLE_ADMIN")) return order;

        CustomerEntity customer = user.getCustomer();
        if (customer != null && order.getCustomer().getId().equals(customer.getId())) {
            return order;
        }

        throw new AccessDeniedException("Вы не можете просматривать этот заказ");
    }

    /**
     * Выборка заказов по заданным критериям
     * @param city город доставки (опционально)
     * @param street улица доставки (опционально)
     * @param zipcode почтовый индекс (опционально)
     * @param startTime начало временного интервала (опционально)
     * @param endTime конец временного интервала (опционально)
     * @param paymentType тип оплаты (опционально: "Cash", "Check", "Credit")
     * @param customerName имя клиента (опционально)
     * @param taxStatus статус налогообложения (опционально)
     * @param orderStatus статус заказа (опционально)
     * @return список заказов, соответствующих критериям
     */
    public List<OrderEntity> findOrdersByCriteria(
            List<OrderEntity> orders,
            String city,
            String street,
            String zipcode,
            Timestamp startTime,
            Timestamp endTime,
            String paymentType,
            String customerName,
            String taxStatus,
            String orderStatus) {

        // Получаем все заказы
//        List<OrderEntity> orders = orderRepository.findAll();

        // Фильтрация по адресу доставки
        if (city != null || street != null || zipcode != null) {
            orders = filterByAddress(orders, city, street, zipcode);
        }

        // Фильтрация по временному интервалу
        if (startTime != null || endTime != null) {
            orders = filterByTimeInterval(orders, startTime, endTime);
        }

        // Фильтрация по способу оплаты
        if (paymentType != null) {
            orders = filterByPaymentType(orders, paymentType);
        }

        // Фильтрация по имени клиента
        if (customerName != null) {
            orders = filterByCustomerName(orders, customerName);
        }

        // Фильтрация по статусу налогообложения
        if (taxStatus != null) {
            orders = filterByTaxStatus(orders, taxStatus);
        }

        // Фильтрация по статусу заказа
        if (orderStatus != null) {
            orders = orders.stream()
                    .filter(order -> order.getStatus().equalsIgnoreCase(orderStatus))
                    .collect(Collectors.toList());
        }

        return orders;
    }

    private List<OrderEntity> filterByAddress(List<OrderEntity> orders, String city, String street, String zipcode) {
        return orders.stream()
                .filter(order -> {
                    CustomerEntity customer = order.getCustomer();
                    if (customer != null && customer.getAddress() != null) {
                        AddressEntity address = customer.getAddress();
                        return (city == null || address.getCity().equalsIgnoreCase(city)) &&
                                (street == null || address.getStreet().equalsIgnoreCase(street)) &&
                                (zipcode == null || address.getZipcode().equalsIgnoreCase(zipcode));
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }

    private List<OrderEntity> filterByTimeInterval(List<OrderEntity> orders, Timestamp startTime, Timestamp endTime) {
        return orders.stream()
                .filter(order -> {
                    Timestamp orderDate = order.getDate();
                    return (startTime == null || !orderDate.before(startTime)) &&
                            (endTime == null || !orderDate.after(endTime));
                })
                .collect(Collectors.toList());
    }

    private List<OrderEntity> filterByPaymentType(List<OrderEntity> orders, String paymentType) {
        return orders.stream()
                .filter(order -> {
                    List<PaymentEntity> payments = paymentRepository.findByOrderId(order.getId());
                    return payments.stream().anyMatch(payment -> {
                        switch (paymentType.toLowerCase()) {
                            case "cash":
                                return cashRepository.existsById(payment.getId());
                            case "check":
                                return checkRepository.existsById(payment.getId());
                            case "credit":
                                return creditRepository.existsById(payment.getId());
                            default:
                                return false;
                        }
                    });
                })
                .collect(Collectors.toList());
    }

    private List<OrderEntity> filterByCustomerName(List<OrderEntity> orders, String customerName) {
        return orders.stream()
                .filter(order -> {
                    CustomerEntity customer = order.getCustomer();
                    return customer != null && customer.getName().equalsIgnoreCase(customerName);
                })
                .collect(Collectors.toList());
    }

    private List<OrderEntity> filterByTaxStatus(List<OrderEntity> orders, String taxStatus) {
        return orders.stream()
                .filter(order -> {
                    List<OrderDetailEntity> details = orderDetailRepository.findByOrderId(order.getId());
                    return details.stream()
                            .anyMatch(detail -> detail.getTaxStatus().equalsIgnoreCase(taxStatus));
                })
                .collect(Collectors.toList());
    }

    public List<OrderEntity> getOrdersByCustomer(CustomerEntity customer) {
        return orderRepository.findByCustomerId(customer.getId());
    }
}