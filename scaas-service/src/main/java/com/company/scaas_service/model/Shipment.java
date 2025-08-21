package com.company.scaas_service.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "shipments")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String trackingNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrier_id")
    private Carrier carrier;
    
    private String origin;
    
    private String destination;
    
    private OffsetDateTime eta;
    
    private String priority;
    
    private String status;
    
    private OffsetDateTime createdAt;
    
    private OffsetDateTime updatedAt;

    // Getters and Setters
}