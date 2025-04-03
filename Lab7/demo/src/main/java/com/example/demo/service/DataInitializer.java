//package com.example.demo.service;
//
//import com.example.demo.repository.*;
//import com.example.demo.entities.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import jakarta.annotation.PostConstruct;
//import jakarta.transaction.Transactional;
//import java.sql.Timestamp;
//import java.time.Instant;
//
//@Service
//public class DataInitializer {
//    @Autowired
//    private AddressRepository addressRepository;
//    @Autowired
//    private CustomerRepository customerRepository;
//    @Autowired
//    private OrderRepository orderRepository;
//    @Autowired
//    private PaymentRepository paymentRepository;
//    @Autowired
//    private ItemRepository itemRepository;
//    @Autowired
//    private MeasurementRepository measurementRepository;
//    @Autowired
//    private OrderDetailsRepository orderDetailsRepository;
//
//    @PostConstruct
//    @Transactional
//    public void initData() {
//        AddressEntity address = new AddressEntity();
//        address.setCity("Moscow");
//        address.setStreet("Tverskaya");
//        address.setZipcode("123456");
//        addressRepository.save(address);
//
//        // Создание клиента
//        CustomerEntity customer = new CustomerEntity();
//        customer.setName("Ivan Ivanov");
//        customer.setAddress(address);
//        customerRepository.save(customer);
//
//        // Создание веса
//        WeightEntity weight = new WeightEntity();
//        weight.setName("Package Weight");
//        weight.setSymbol("kg");
//        weight.setValue(5L);
//        measurementRepository.save(weight);
//
//        // Создание товара
//        ItemEntity item = new ItemEntity();
//        item.setDescription("Test Product");
//        item.setShippingWeight(weight);
//        itemRepository.save(item);
//
//        // Создание заказа
//        OrderEntity order = new OrderEntity();
//        order.setCustomer(customer);
//        order.setDate(Timestamp.from(Instant.now()));
//        order.setStatus("DELIVERED");
//        orderRepository.save(order);
//
//        // Создание оплаты наличными
//        CashEntity cashPayment = new CashEntity();
//        cashPayment.setOrder(order);
//        cashPayment.setAmount(100.0f);
//        cashPayment.setCashTendered(150.0f);
//        paymentRepository.save(cashPayment);
//
//        QuantityEntity quantity = new QuantityEntity();
//        quantity.setName("Items Count");
//        quantity.setSymbol("pcs");
//        quantity.setValue(10);
//        measurementRepository.save(quantity);
//
//        // Создание OrderDetail
//        OrderDetailEntity orderDetail = new OrderDetailEntity();
//        orderDetail.setOrder(order);
//        orderDetail.setItem(item);
//        orderDetail.setQuantity(quantity);
//        orderDetail.setTaxStatus("VAT_20");
//        orderDetailsRepository.save(orderDetail);
//    }
//}
