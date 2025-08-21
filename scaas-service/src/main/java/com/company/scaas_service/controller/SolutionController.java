package com.company.scaas_service.controller;

import com.company.scaas_service.model.Solution;
import com.company.scaas_service.repository.SolutionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events") // Base path for event-related actions
public class SolutionController {

    private final SolutionRepository solutionRepository;

    public SolutionController(SolutionRepository solutionRepository) {
        this.solutionRepository = solutionRepository;
    }

    @GetMapping("/{eventId}/solutions")
    public ResponseEntity<List<Solution>> getSolutionsForEvent(@PathVariable Long eventId) {
        List<Solution> solutions = solutionRepository.findByEventId(eventId);
        return ResponseEntity.ok(solutions);
    }
}