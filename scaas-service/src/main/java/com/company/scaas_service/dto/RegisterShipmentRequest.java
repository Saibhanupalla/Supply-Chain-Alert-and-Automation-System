package com.company.scaas_service.dto;

import java.time.OffsetDateTime;

public record RegisterShipmentRequest(
    String trackingNumber,
    Long carrierId,
    String origin,
    String destination,
    OffsetDateTime eta,
    String priority
) {}