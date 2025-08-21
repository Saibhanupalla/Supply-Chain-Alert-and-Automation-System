package com.company.scaas_service.model;

import jakarta.persistence.*;
import lombok.Data;

@Data

@Entity
@Table(name = "carriers")
public class Carrier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String baseUrl;
    private String apiKey;
    private boolean active;

    // Getters and Setters
}