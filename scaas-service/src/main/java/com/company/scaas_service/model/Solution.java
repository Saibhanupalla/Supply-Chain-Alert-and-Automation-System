package com.company.scaas_service.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.Data;

@Data

@Entity
@Table(name = "solutions")
public class Solution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "alt_carrier_id")
    private Carrier altCarrier;

    private BigDecimal quotedPrice;
    private OffsetDateTime quotedEta;

    @Column(columnDefinition = "TEXT")
    private String quotePayloadJson;

    private boolean chosen;

    // Getters and Setters
}