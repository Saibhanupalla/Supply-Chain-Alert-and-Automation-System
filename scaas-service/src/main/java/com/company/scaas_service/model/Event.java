package com.company.scaas_service.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore; 

@Data

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id")
    private Shipment shipment;

    private String externalStatus;
    private String internalStatus;
    
    @Column(columnDefinition = "TEXT")
    private String rawPayloadJson;

    private OffsetDateTime detectedAt;

    // Getters and Setters
}