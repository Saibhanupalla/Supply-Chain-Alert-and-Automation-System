package com.company.scaas_service.repository;

import com.company.scaas_service.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertRepository extends JpaRepository<Alert, Long> {
}