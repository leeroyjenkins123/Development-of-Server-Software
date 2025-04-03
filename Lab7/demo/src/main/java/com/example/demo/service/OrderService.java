package com.example.demo.service;

import com.example.demo.entities.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final PaymentRepository paymentRepository;
    private final CashRepository cashRepository;
    private final CheckRepository checkRepository;
    private final CreditRepository creditRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        OrderDetailsRepository orderDetailsRepository,
                        CustomerRepository customerRepository,
                        AddressRepository addressRepository,
                        PaymentRepository paymentRepository,
                        CashRepository cashRepository,
                        CheckRepository checkRepository,
                        CreditRepository creditRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailsRepository = orderDetailsRepository;
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
        this.paymentRepository = paymentRepository;
        this.cashRepository = cashRepository;
        this.checkRepository = checkRepository;
        this.creditRepository = creditRepository;
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
        List<OrderEntity> orders = orderRepository.findAll();

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
                    List<OrderDetailEntity> details = orderDetailsRepository.findByOrderId(order.getId());
                    return details.stream()
                            .anyMatch(detail -> detail.getTaxStatus().equalsIgnoreCase(taxStatus));
                })
                .collect(Collectors.toList());
    }
}