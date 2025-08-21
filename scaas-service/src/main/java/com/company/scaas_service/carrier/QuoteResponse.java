package com.company.scaas_service.carrier;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

// By making this a public record, it's visible to all other classes
public record QuoteResponse(BigDecimal price, OffsetDateTime estimatedEta, String rawJson) {}