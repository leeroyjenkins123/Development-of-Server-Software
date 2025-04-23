package com.example.demo.service.customer;

import com.example.demo.entity.customer.AddressEntity;
import com.example.demo.entity.customer.CustomerEntity;
import com.example.demo.entity.user.UserEntity;
import com.example.demo.exception.AccessDeniedException;
import com.example.demo.repository.customer.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = true)
    public List<CustomerEntity> getAllAccessibleCustomers(UserEntity user) {
        if (user.getRole().getRole().equals("ROLE_ADMIN")) {
            return customerRepository.findAll();
        }
        else if (user.getCustomer() != null) {
            CustomerEntity customer = user.getCustomer();
            return List.of(customer);
        }
        return List.of();
    }

    public CustomerEntity getCustomerByIdIfAuthorized(Long id, UserEntity user) throws AccessDeniedException {
        Optional<CustomerEntity> optional = customerRepository.findById(id);
        if (optional.isEmpty()) {
            return null;
        }

        CustomerEntity customer = optional.get();

        if (user.getRole().getRole().equals("ROLE_ADMIN") ||
                (user.getCustomer() != null && user.getCustomer().getId().equals(id))) {
            return customer;
        }

        if (user.getCustomer() != null && customer.getId().equals(user.getCustomer().getId())) {
            return customer;
        }

        throw new AccessDeniedException("You don't have permission to access this customer");
    }

    public CustomerEntity saveCustomer(CustomerEntity customer) {
        return customerRepository.save(customer);
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}