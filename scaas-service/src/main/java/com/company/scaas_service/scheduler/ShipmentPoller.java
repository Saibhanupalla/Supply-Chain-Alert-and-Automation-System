package com.company.scaas_service.scheduler;

import com.company.scaas_service.carrier.CarrierClient;
import com.company.scaas_service.carrier.CarrierStatus;
import com.company.scaas_service.event.DisruptionDetectedEvent;
import com.company.scaas_service.model.Event;
import com.company.scaas_service.model.Shipment;
import com.company.scaas_service.repository.EventRepository;
import com.company.scaas_service.repository.ShipmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import java.util.HexFormat;
import java.util.Set;

@Component
public class ShipmentPoller {

    private static final Logger log = LoggerFactory.getLogger(ShipmentPoller.class);
    private final ShipmentRepository shipmentRepository;
    private final EventRepository eventRepository;
    private final CarrierClient carrierClient;
    private final StringRedisTemplate redisTemplate;
    private final ApplicationEventPublisher eventPublisher;

    public ShipmentPoller(ShipmentRepository shipmentRepository,
                          EventRepository eventRepository,
                          @Qualifier("mockCarrierClient") CarrierClient carrierClient, // Using our mock for now
                          StringRedisTemplate redisTemplate,
                          ApplicationEventPublisher eventPublisher) {
        this.shipmentRepository = shipmentRepository;
        this.eventRepository = eventRepository;
        this.carrierClient = carrierClient;
        this.redisTemplate = redisTemplate;
        this.eventPublisher = eventPublisher;
    }

    @Scheduled(fixedDelayString = "PT2M")
    public void pollActiveShipments() {
        log.info("Polling for active shipment updates...");
        // For V1, let's assume any non-DELIVERED shipment is "active"
        for (Shipment shipment : shipmentRepository.findByStatusNot("DELIVERED")) {
            carrierClient.fetchStatus(shipment.getTrackingNumber())
                .ifPresent(status -> processStatusUpdate(shipment, status));
        }
        log.info("Polling complete.");
    }

    private void processStatusUpdate(Shipment shipment, CarrierStatus status) {
        // IDEMPOTENCY CHECK using Redis [cite: 112]
        String payloadHash = hashPayload(status.rawJson());
        String redisKey = "scaas:shipment:" + shipment.getId() + ":last_hash";
        String lastSeenHash = redisTemplate.opsForValue().get(redisKey);

        if (payloadHash.equals(lastSeenHash)) {
            log.info("Shipment {} status unchanged. Skipping.", shipment.getId());
            return;
        }

        // PERSIST the new event [cite: 115]
        Event newEvent = new Event();
        newEvent.setShipment(shipment);
        newEvent.setExternalStatus(status.externalStatus());
        newEvent.setInternalStatus(mapExternalToInternalStatus(status.externalStatus())); // Map to our own statuses
        newEvent.setRawPayloadJson(status.rawJson());
        newEvent.setDetectedAt(status.lastUpdateTime());
        eventRepository.save(newEvent);
        
        // Update the shipment's main status
        shipment.setStatus(newEvent.getInternalStatus());
        shipmentRepository.save(shipment);

        redisTemplate.opsForValue().set(redisKey, payloadHash);
        log.info("New event for Shipment {} persisted with status '{}'", shipment.getId(), newEvent.getInternalStatus());

        // PUBLISH event if it's a disruption [cite: 117]
        Set<String> disruptionStatuses = Set.of("DELAYED", "HELD_AT_CUSTOMS");
        if (disruptionStatuses.contains(newEvent.getInternalStatus())) {
            eventPublisher.publishEvent(new DisruptionDetectedEvent(this, shipment, newEvent));
            log.info("Disruption event published for Shipment {}", shipment.getId());
        }
    }
    
    // Helper to map carrier-specific statuses to our internal, standardized ones [cite: 113]
    private String mapExternalToInternalStatus(String externalStatus) {
        if (externalStatus == null) return "UNKNOWN";
        return externalStatus.toUpperCase().replace(" ", "_");
    }

    // Helper to create a SHA-256 hash for the idempotency check [cite: 112]
    private String hashPayload(String payload) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(payload.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}