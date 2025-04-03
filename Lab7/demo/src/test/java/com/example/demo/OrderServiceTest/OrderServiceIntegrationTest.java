package com.example.demo.OrderServiceTest;

import com.example.demo.OrderServiceTest.AbstractIntegrationTest;
import com.example.demo.entities.*;
import com.example.demo.service.*;
import com.example.demo.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CashRepository cashRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private QuantityRepository quantityRepository;

    @Autowired
    private MeasurementRepository measurementRepository;

    @BeforeEach
    void setUp() {
        // Очистка таблиц перед каждым тестом
        orderDetailsRepository.deleteAll();
        paymentRepository.deleteAll();
        orderRepository.deleteAll();
        customerRepository.deleteAll();
        addressRepository.deleteAll();
        itemRepository.deleteAll();
        quantityRepository.deleteAll();

        // Создание тестовых данных
        AddressEntity address = new AddressEntity();
        address.setCity("Moscow");
        address.setStreet("Lenin St");
        address.setZipcode("123456");
        addressRepository.save(address);

        CustomerEntity customer = new CustomerEntity();
        customer.setName("John Doe");
        customer.setAddress(address);
        customerRepository.save(customer);

        OrderEntity order = new OrderEntity();
        order.setCustomer(customer);
        order.setDate(Timestamp.from(Instant.now()));
        order.setStatus("SHIPPED");
        orderRepository.save(order);

        WeightEntity weight = new WeightEntity();
        weight.setName("Package Weight");
        weight.setSymbol("kg");
        weight.setValue(5L);
        measurementRepository.save(weight);

        ItemEntity item = new ItemEntity();
        item.setDescription("Test Item");
        item.setShippingWeight(weight);
        itemRepository.save(item);

        QuantityEntity quantityEntity = new QuantityEntity();
        quantityEntity.setName("Items Count");
        quantityEntity.setSymbol("pcs");
        quantityEntity.setValue(10);
        measurementRepository.save(quantityEntity);

        OrderDetailEntity orderDetail = new OrderDetailEntity();
        orderDetail.setOrder(order);
        orderDetail.setItem(item);
        orderDetail.setQuantity(quantityEntity);
        orderDetail.setTaxStatus("TAXABLE");
        orderDetailsRepository.save(orderDetail);

        CashEntity cashPayment = new CashEntity();
        cashPayment.setOrder(order);
        cashPayment.setAmount(100.0f);
        cashPayment.setCashTendered(120.0f);
        paymentRepository.save(cashPayment);
    }

    @Test
    void testFindOrdersByCity() {
        List<OrderEntity> orders = orderService.findOrdersByCriteria(
                "Moscow", null, null, null, null, null, null, null, null);
        assertEquals(1, orders.size());
        assertEquals("Moscow", orders.getFirst().getCustomer().getAddress().getCity());
    }

    @Test
    void testFindOrdersByTimeInterval() {
        Timestamp startTime = Timestamp.from(Instant.now().minusSeconds(3600)); // 1 час назад
        Timestamp endTime = Timestamp.from(Instant.now().plusSeconds(3600));   // 1 час вперед
        List<OrderEntity> orders = orderService.findOrdersByCriteria(
                null, null, null, startTime, endTime, null, null, null, null);
        assertEquals(1, orders.size());
    }

    @Test
    void testFindOrdersByPaymentType() {
        List<OrderEntity> orders = orderService.findOrdersByCriteria(
                null, null, null, null, null, "Cash", null, null, null);
        assertEquals(1, orders.size());
    }

    @Test
    void testFindOrdersByCustomerName() {
        List<OrderEntity> orders = orderService.findOrdersByCriteria(
                null, null, null, null, null, null, "John Doe", null, null);
        assertEquals(1, orders.size());
        assertEquals("John Doe", orders.getFirst().getCustomer().getName());
    }

    @Test
    void testFindOrdersByTaxStatus() {
        List<OrderEntity> orders = orderService.findOrdersByCriteria(
                null, null, null, null, null, null, null, "TAXABLE", null);
        assertEquals(1, orders.size());
    }

    @Test
    void testFindOrdersByOrderStatus() {
        List<OrderEntity> orders = orderService.findOrdersByCriteria(
                null, null, null, null, null, null, null, null, "SHIPPED");
        assertEquals(1, orders.size());
        assertEquals("SHIPPED", orders.getFirst().getStatus());
    }

    @Test
    void testFindOrdersByMultipleCriteria() {
        Timestamp startTime = Timestamp.from(Instant.now().minusSeconds(3600));
        Timestamp endTime = Timestamp.from(Instant.now().plusSeconds(3600));
        List<OrderEntity> orders = orderService.findOrdersByCriteria(
                "Moscow", null, null, startTime, endTime, "Cash", "John Doe", "TAXABLE", "SHIPPED");
        assertEquals(1, orders.size());
    }

    @Test
    void testFindOrdersNoMatch() {
        List<OrderEntity> orders = orderService.findOrdersByCriteria(
                "Berlin", null, null, null, null, null, null, null, null);
        assertEquals(0, orders.size());
    }
}