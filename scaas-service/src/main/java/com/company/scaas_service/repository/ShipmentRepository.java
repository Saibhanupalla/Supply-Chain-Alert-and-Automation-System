package com.company.scaas_service.repository;

import com.company.scaas_service.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    // We can add custom queries here later, e.g., to find active shipments
    List<Shipment> findByStatusNot(String status);
}