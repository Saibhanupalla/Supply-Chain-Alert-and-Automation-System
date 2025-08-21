package com.company.scaas_service.controller;

import com.company.scaas_service.dto.RegisterShipmentRequest;
import com.company.scaas_service.model.Shipment;
import com.company.scaas_service.service.ShipmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.company.scaas_service.repository.SolutionRepository;

@RestController
@RequestMapping("/api/v1/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;
    private final SolutionRepository solutionRepository;

    public ShipmentController(ShipmentService shipmentService, SolutionRepository solutionRepository) { // <-- UPDATE CONSTRUCTOR
        this.shipmentService = shipmentService;
        this.solutionRepository = solutionRepository; // <-- ADD THIS
    }

    @PostMapping
    public ResponseEntity<Shipment> registerShipment(@RequestBody RegisterShipmentRequest request) {
        Shipment newShipment = shipmentService.registerNewShipment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newShipment);
    }
    @PostMapping("/test/simulate-delay/{shipmentId}")
    public ResponseEntity<String> simulateDelay(@PathVariable Long shipmentId) { // <-- Change return type
        shipmentService.simulateDelay(shipmentId);
        return ResponseEntity.ok("Delay successfully simulated for shipment " + shipmentId); // <-- Return a simple message
    }
}