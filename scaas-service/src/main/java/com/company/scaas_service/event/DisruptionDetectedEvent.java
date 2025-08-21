package com.company.scaas_service.event;

import com.company.scaas_service.model.Event;
import com.company.scaas_service.model.Shipment;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter // <-- Use Lombok here too for cleaner code!
public class DisruptionDetectedEvent extends ApplicationEvent {
    private final Shipment shipment;
    private final Event event;

    public DisruptionDetectedEvent(Object source, Shipment shipment, Event event) {
        super(source);
        this.shipment = shipment;
        this.event = event;
    }
}