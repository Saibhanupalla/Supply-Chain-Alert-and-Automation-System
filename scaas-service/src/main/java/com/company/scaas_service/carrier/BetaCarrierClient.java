package com.company.scaas_service.carrier;

import com.company.scaas_service.dto.QuoteRequest;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;

@Service("betaCarrierClient")
public class BetaCarrierClient implements CarrierClient {
    @Override
    public Optional<CarrierStatus> fetchStatus(String trackingNumber) { return Optional.empty(); }

    @Override
    public Optional<QuoteResponse> getQuote(QuoteRequest request) {
        // Simulate a faster but more expensive option
        var price = new BigDecimal("150.75");
        var eta = OffsetDateTime.now().plusDays(3);
        var json = "{\"carrier\":\"Beta\",\"price\":150.75}";
        return Optional.of(new QuoteResponse(price, eta, json));
    }
}