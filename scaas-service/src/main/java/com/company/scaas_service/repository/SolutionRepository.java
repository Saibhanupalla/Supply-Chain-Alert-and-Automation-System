package com.company.scaas_service.repository;

import com.company.scaas_service.model.Solution;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SolutionRepository extends JpaRepository<Solution, Long> {
    List<Solution> findByEventId(Long eventId);
}