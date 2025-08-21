package com.company.scaas_service.carrier;

import com.company.scaas_service.dto.QuoteRequest;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;

@Service("gammaCarrierClient")
public class GammaCarrierClient implements CarrierClient {
    @Override
    public Optional<CarrierStatus> fetchStatus(String trackingNumber) { return Optional.empty(); }

    @Override
    public Optional<QuoteResponse> getQuote(QuoteRequest request) {
        // Simulate a slower but cheaper option
        var price = new BigDecimal("99.50");
        var eta = OffsetDateTime.now().plusDays(5);
        var json = "{\"carrier\":\"Gamma\",\"price\":99.50}";
        return Optional.of(new QuoteResponse(price, eta, json));
    }
}