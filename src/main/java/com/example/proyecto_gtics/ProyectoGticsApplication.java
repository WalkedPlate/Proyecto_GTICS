package com.example.proyecto_gtics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ProyectoGticsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProyectoGticsApplication.class, args);
	}

}
