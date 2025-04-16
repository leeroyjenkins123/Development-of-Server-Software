package com.example.demo.dto.order;

import lombok.*;
import java.sql.Timestamp;

@Data
public class OrderResponse {
    private Long id;
    private Timestamp date;
    private String status;
    private Long customerId;
    private String customerName;
}
