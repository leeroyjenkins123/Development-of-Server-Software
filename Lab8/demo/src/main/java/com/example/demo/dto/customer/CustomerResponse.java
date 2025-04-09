package com.example.demo.dto.customer;

import com.example.demo.entity.customer.AddressEntity;
import lombok.*;

@Data
public class CustomerResponse {
    private Long id;
    private String name;
    private AddressEntity address;
}
