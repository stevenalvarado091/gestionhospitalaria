package com.stiveen.gestionhospitalaria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GestionhospitalariaApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionhospitalariaApplication.class, args);
	}

}
