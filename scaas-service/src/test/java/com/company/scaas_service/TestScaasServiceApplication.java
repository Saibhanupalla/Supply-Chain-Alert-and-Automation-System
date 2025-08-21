package com.company.scaas_service;

import org.springframework.boot.SpringApplication;

public class TestScaasServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(ScaasServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
