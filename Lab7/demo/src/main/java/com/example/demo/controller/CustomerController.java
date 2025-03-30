package com.example.demo.controller;

import com.example.demo.entities.CustomerEntity;
import com.example.demo.repository.CustomerRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customer Management", description = "APIs for managing customers")
public class CustomerController {

    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping
    @Operation(summary = "Get all customers")
    public List<CustomerEntity> getAllCustomers() {
        return customerRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID")
    public Optional<CustomerEntity> getCustomerById(@PathVariable Long id) {
        return customerRepository.findById(id);
    }

    @PostMapping
    @Operation(summary = "Create a new customer")
    public CustomerEntity createCustomer(@RequestBody CustomerEntity customer) {
        return customerRepository.save(customer);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update customer by ID")
    public CustomerEntity updateCustomer(@PathVariable Long id, @RequestBody CustomerEntity updatedCustomer) {
        updatedCustomer.setId(id); // Убедитесь, что сущность имеет метод setId()
        return customerRepository.save(updatedCustomer);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete customer by ID")
    public void deleteCustomer(@PathVariable Long id) {
        customerRepository.deleteById(id);
    }
}