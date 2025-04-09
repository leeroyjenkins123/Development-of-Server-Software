package com.example.demo.dto.customer;

import lombok.Data;

@Data
public class AddressResponse {
    private Long id;
    private String city;
    private String street;
    private String zipcode;
}
