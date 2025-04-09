package com.example.demo.service.customer;

import com.example.demo.entity.customer.AddressEntity;
import com.example.demo.entity.customer.CustomerEntity;
import com.example.demo.entity.user.UserEntity;
import com.example.demo.exception.AccessDeniedException;
import com.example.demo.repository.customer.AddressRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AddressService {

    private final AddressRepository addressRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Transactional(readOnly = true)
    public List<AddressEntity> getAllAccessibleAddresses(UserEntity user) {
        if (user.getRole().getRole().equals("ROLE_ADMIN")) {
            return addressRepository.findAll();
        }
        else if (user.getCustomer() != null && user.getCustomer().getAddress() != null) {
            AddressEntity address = user.getCustomer().getAddress();
            return List.of(address);
        }
        return List.of();
    }

    public AddressEntity getAddressByIdIfAuthorized(Long id, UserEntity user) throws AccessDeniedException {
        Optional<AddressEntity> optional = addressRepository.findById(id);
        if (optional.isEmpty()) {
            return null;
        }

        AddressEntity address = optional.get();

        if (user.getRole().getRole().equals("ROLE_ADMIN")) {
            return address;
        }


        if (user.getCustomer() != null && address.getId().equals(user.getCustomer().getAddress().getId())) {
            return address;
        }

        throw new AccessDeniedException("You don't have permission to access this address");
    }

    public AddressEntity saveAddress(AddressEntity address) {
        return addressRepository.save(address);
    }

    public void deleteAddress(Long id) {
        addressRepository.deleteById(id);
    }
}