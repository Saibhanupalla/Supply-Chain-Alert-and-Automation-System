package com.company.scaas_service.repository;

import com.company.scaas_service.model.Carrier;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CarrierRepository extends JpaRepository<Carrier, Long> {
    Optional<Carrier> findByName(String name);
}