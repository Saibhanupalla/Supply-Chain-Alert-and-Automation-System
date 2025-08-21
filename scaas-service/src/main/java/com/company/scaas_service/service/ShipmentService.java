package com.company.scaas_service.service;

import com.company.scaas_service.dto.RegisterShipmentRequest;
import com.company.scaas_service.event.DisruptionDetectedEvent;
import com.company.scaas_service.model.Event;
import com.company.scaas_service.model.Shipment;
import com.company.scaas_service.repository.CarrierRepository;
import com.company.scaas_service.repository.EventRepository;
import com.company.scaas_service.repository.ShipmentRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final CarrierRepository carrierRepository;
    private final EventRepository eventRepository; // <-- ADD
    private final ApplicationEventPublisher eventPublisher; // <-- ADD

    public ShipmentService(ShipmentRepository shipmentRepository, CarrierRepository carrierRepository, EventRepository eventRepository, ApplicationEventPublisher eventPublisher) {
        this.shipmentRepository = shipmentRepository;
        this.carrierRepository = carrierRepository;
        this.eventRepository = eventRepository; // <-- ADD
        this.eventPublisher = eventPublisher; // <-- ADD
    }

    @Transactional
    public Shipment registerNewShipment(RegisterShipmentRequest request) {
        var carrier = carrierRepository.findById(request.carrierId())
            .orElseThrow(() -> new IllegalArgumentException("Invalid Carrier ID: " + request.carrierId()));

        Shipment newShipment = new Shipment();
        newShipment.setTrackingNumber(request.trackingNumber());
        newShipment.setCarrier(carrier);
        newShipment.setOrigin(request.origin());
        newShipment.setDestination(request.destination());
        newShipment.setEta(request.eta());
        newShipment.setPriority(request.priority());
        newShipment.setStatus("REGISTERED");
        newShipment.setCreatedAt(OffsetDateTime.now());
        newShipment.setUpdatedAt(OffsetDateTime.now());

        return shipmentRepository.save(newShipment);
    }

    @Transactional
    public void simulateDelay(Long shipmentId) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid Shipment ID: " + shipmentId));

        shipment.setStatus("DELAYED");

        Event testEvent = new Event();
        testEvent.setShipment(shipment);
        testEvent.setInternalStatus("DELAYED");
        testEvent.setDetectedAt(OffsetDateTime.now());
        
        // Save the event to the DB to get an ID
        Event savedEvent = eventRepository.save(testEvent);

        // Publish the event with the saved entity
        eventPublisher.publishEvent(new DisruptionDetectedEvent(this, shipment, savedEvent));

    }
}