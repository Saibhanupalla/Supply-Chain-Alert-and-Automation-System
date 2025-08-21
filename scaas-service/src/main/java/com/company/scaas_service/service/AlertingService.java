package com.company.scaas_service.service;

import com.company.scaas_service.event.DisruptionDetectedEvent;
import com.company.scaas_service.model.Alert;
import com.company.scaas_service.repository.AlertRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.company.scaas_service.service.SolutionService;

import java.time.OffsetDateTime;

@Service
public class AlertingService {

    private static final Logger log = LoggerFactory.getLogger(AlertingService.class);
    private final JavaMailSender mailSender;
    private final AlertRepository alertRepository; // Create this repository interface
    private final SolutionService solutionService;

    public AlertingService(JavaMailSender mailSender, AlertRepository alertRepository, SolutionService solutionService) { // <-- UPDATE CONSTRUCTOR
        this.mailSender = mailSender;
        this.alertRepository = alertRepository;
        this.solutionService = solutionService; // <-- ADD THIS
    }

    @EventListener
    public void onDisruptionDetected(DisruptionDetectedEvent event) {
        log.info("Received disruption event for shipment {}. Sending email alert.", event.getShipment().getId());

        // 1. Send the email alert
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@scaas.com");
        message.setTo("supply-chain-manager@company.com"); // Configurable in a real app
        message.setSubject("Shipment Disruption Alert: " + event.getShipment().getTrackingNumber());
        message.setText(
            "A disruption has been detected for shipment with tracking number: "
            + event.getShipment().getTrackingNumber()
            + "\nNew Status: " + event.getEvent().getInternalStatus()
            + "\nDetected At: " + event.getEvent().getDetectedAt()
            + "\n\nA link to retrieve solutions: http://localhost:8080/api/v1/events/"
            + event.getEvent().getId() + "/solutions"
        );
        mailSender.send(message);

        // 2. Record that an alert was sent [cite: 120]
        Alert newAlert = new Alert();
        newAlert.setShipment(event.getShipment());
        newAlert.setEvent(event.getEvent());
        newAlert.setSeverity("HIGH"); // Could be based on shipment priority
        newAlert.setEmailSentAt(OffsetDateTime.now());
        alertRepository.save(newAlert);

        // 3. Find and rank alternative solutions
        solutionService.findAndRankSolutions(event.getEvent());

        log.info("Email sent and alert recorded for shipment {}", event.getShipment().getId());
    }
}