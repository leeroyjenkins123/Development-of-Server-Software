package com.example.demo.controller.customer;

import com.example.demo.dto.customer.AddressResponse;
import com.example.demo.dto.order.OrderResponse;
import com.example.demo.entity.customer.AddressEntity;
import com.example.demo.entity.user.UserEntity;
import com.example.demo.exception.AccessDeniedException;
import com.example.demo.repository.user.UserRepository;
import com.example.demo.service.customer.AddressService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/addresses")
@Tag(name = "Addresses", description = "Endpoints for address management")
@SecurityRequirement(name = "bearerAuth")
@Slf4j
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AddressResponse>> getAllAddresses(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getClaimAsString("preferred_username"); // или "sub", если username хранится там
        log.info("Username from token: {}", username);
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        List<AddressEntity> addresses = addressService.getAllAccessibleAddresses(user);
        List<AddressResponse> dtos = addresses.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(dtos);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AddressResponse> getAddressById(@PathVariable Long id,
                                                          @AuthenticationPrincipal Jwt jwt) throws AccessDeniedException {
        String username = jwt.getClaimAsString("preferred_username"); // или "sub", если username хранится там
        log.info("Username from token: {}", username);
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        AddressEntity address = addressService.getAddressByIdIfAuthorized(id, user);
        AddressResponse response = convertToDTO(address);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AddressResponse> createAddress(@RequestBody AddressEntity address) {
        AddressEntity saved = addressService.saveAddress(address);
        AddressResponse response = convertToDTO(address);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }

    private AddressResponse convertToDTO(AddressEntity address) {
        AddressResponse response = new AddressResponse();
        response.setId(address.getId());
        response.setCity(address.getCity());
        response.setStreet(address.getStreet());
        response.setZipcode(address.getZipcode());
        return response;
    }
}