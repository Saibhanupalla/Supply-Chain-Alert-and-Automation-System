package com.company.scaas_service.carrier;

import com.company.scaas_service.dto.QuoteRequest;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import java.util.Optional;

@Service("mockCarrierClient")
public class MockCarrierClient implements CarrierClient {

    @Override
    public Optional<CarrierStatus> fetchStatus(String trackingNumber) {
        // In a real app, you'd make an HTTP call here.
        // For now, let's simulate a delay.
        String status = "DELAYED";
        String rawJson = "{\"status\":\"DELAYED\", \"location\":\"CUSTOMS_PORT_NYC\"}";
        return Optional.of(new CarrierStatus(status, rawJson, OffsetDateTime.now()));
    }

    // ADD THIS METHOD
    @Override
    public Optional<QuoteResponse> getQuote(QuoteRequest request) {
        // This is the original carrier, so it doesn't provide an alternative quote.
        return Optional.empty();
    }
}