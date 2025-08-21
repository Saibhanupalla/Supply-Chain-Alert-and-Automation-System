package com.company.scaas_service.carrier;

import com.company.scaas_service.dto.QuoteRequest;
import java.util.Optional;


public interface CarrierClient {
    Optional<CarrierStatus> fetchStatus(String trackingNumber);
    Optional<QuoteResponse> getQuote(QuoteRequest request); 
}