package dev.projectfitness.ipfitness;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class IpFitnessApplication {

	public static void main(String[] args) {
		SpringApplication.run(IpFitnessApplication.class, args);
	}

}
