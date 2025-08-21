package com.company.scaas_service.carrier;

import java.time.OffsetDateTime;

// By making this a public record in its own file, it's visible everywhere.
public record CarrierStatus(String externalStatus, String rawJson, OffsetDateTime lastUpdateTime) {}