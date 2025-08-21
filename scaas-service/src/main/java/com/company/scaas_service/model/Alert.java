package com.company.scaas_service.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import lombok.Data;

@Data

@Entity
@Table(name = "alerts")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id")
    private Shipment shipment;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    private String severity;
    private OffsetDateTime emailSentAt;
    private boolean acknowledged;

    // Getters and Setters
}