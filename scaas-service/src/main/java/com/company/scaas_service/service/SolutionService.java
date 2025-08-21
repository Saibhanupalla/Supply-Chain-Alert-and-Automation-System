package com.company.scaas_service.service;

import com.company.scaas_service.carrier.CarrierClient;
import com.company.scaas_service.dto.QuoteRequest;
import com.company.scaas_service.model.Event;
import com.company.scaas_service.model.Solution;
import com.company.scaas_service.repository.CarrierRepository;
import com.company.scaas_service.repository.SolutionRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class SolutionService {
    private final CarrierRepository carrierRepository;
    private final SolutionRepository solutionRepository;
    private final ApplicationContext context;

    public SolutionService(CarrierRepository carrierRepository, SolutionRepository solutionRepository, ApplicationContext context) {
        this.carrierRepository = carrierRepository;
        this.solutionRepository = solutionRepository;
        this.context = context;
    }

    public void findAndRankSolutions(Event event) {
        var shipment = event.getShipment();
        var quoteRequest = new QuoteRequest(shipment.getOrigin(), shipment.getDestination(), shipment.getPriority());
        var allCarriers = carrierRepository.findAll();
        var solutions = new ArrayList<Solution>();

        for (var carrier : allCarriers) {
            // Don't get a quote from the original carrier that had the delay
            if (carrier.getId().equals(shipment.getCarrier().getId())) {
                continue;
            }

            // Get the correct CarrierClient bean by its name (e.g., "betaCarrierClient")
            try {
                String beanName = carrier.getName().toLowerCase() + "CarrierClient";
                CarrierClient client = context.getBean(beanName, CarrierClient.class);

                client.getQuote(quoteRequest).ifPresent(quote -> {
                    Solution solution = new Solution();
                    solution.setEvent(event);
                    solution.setAltCarrier(carrier);
                    solution.setQuotedPrice(quote.price());
                    solution.setQuotedEta(quote.estimatedEta());
                    solution.setQuotePayloadJson(quote.rawJson());
                    solutions.add(solution);
                });
            } catch (Exception e) {
                // Bean not found, etc.
            }
        }

        // Simple ranking: prefer the fastest arrival time
        solutions.sort(Comparator.comparing(Solution::getQuotedEta));

        // Save the top 3 solutions to the database
        List<Solution> topSolutions = solutions.stream().limit(3).toList();
        solutionRepository.saveAll(topSolutions);
    }
}