package com.company.scaas_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import com.company.scaas_service.model.Carrier;
import com.company.scaas_service.repository.CarrierRepository;

import io.github.cdimascio.dotenv.Dotenv;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
@SpringBootApplication
@EnableScheduling // <-- ADD THIS ANNOTATION
public class ScaasServiceApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().directory("../").ignoreIfMissing().load();
        dotenv.entries().forEach(entry -> {
            System.setProperty(entry.getKey(), entry.getValue());
        });
        SpringApplication.run(ScaasServiceApplication.class, args);
    }
    @Bean
CommandLineRunner seedData(CarrierRepository carrierRepository) {
    return args -> {
        // Check for lowercase "mock"
        if (carrierRepository.findByName("mock").isEmpty()) {
            var mock = new Carrier();
            mock.setName("mock");
            mock.setActive(true);
            carrierRepository.save(mock);
        }
        // Check for lowercase "beta"
        if (carrierRepository.findByName("beta").isEmpty()) {
            var beta = new Carrier();
            beta.setName("beta");
            beta.setActive(true);
            carrierRepository.save(beta);
        }
        // Check for lowercase "gamma"
        if (carrierRepository.findByName("gamma").isEmpty()) {
            var gamma = new Carrier();
            gamma.setName("gamma");
            gamma.setActive(true);
            carrierRepository.save(gamma);
        }
        System.out.println("--- All mock carriers checked/seeded ---");
    };
}
}