package com.example.demo.service;

import com.example.demo.model.Client;
import com.example.demo.model.Supplier;
import com.example.demo.model.Contract;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.SupplierRepository;
import com.example.demo.repository.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
public class DataInitializer {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ContractRepository contractRepository;

    @PostConstruct
    @Transactional
    public void initData() {
        // Client 1
        Client client1 = new Client();
        client1.setName("John Doe");
        clientRepository.save(client1);

        // Supplier 1
        Supplier supplier1 = new Supplier();
        supplier1.setProduct("Laptops");
        supplier1.setQuantity(100);
        supplierRepository.save(supplier1);

        // Contract 1
        Contract contract1 = new Contract();
        contract1.setAmount(5000);
        contract1.setClient(client1);
        contract1.setSupplier(supplier1);
        System.out.println("Saving contract1 with amount: " + contract1.getAmount());
        contractRepository.save(contract1);

        // Client 2
        Client client2 = new Client();
        client2.setName("Jane Smith");
        clientRepository.save(client2);

        // Supplier 2
        Supplier supplier2 = new Supplier();
        supplier2.setProduct("Smartphones");
        supplier2.setQuantity(1000);
        supplierRepository.save(supplier2);

        // Contract 2
        Contract contract2 = new Contract();
        contract2.setAmount(3000);
        contract2.setClient(client2);
        contract2.setSupplier(supplier2);
        System.out.println("Saving contract2 with amount: " + contract2.getAmount());
        contractRepository.save(contract2);
    }
}