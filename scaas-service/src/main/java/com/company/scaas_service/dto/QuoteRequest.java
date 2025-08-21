package com.company.scaas_service.dto;

public record QuoteRequest(
    String origin,
    String destination,
    String priority
    // In a real app, you'd add weight, dimensions, etc.
) {}