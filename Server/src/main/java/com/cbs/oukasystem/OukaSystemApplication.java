package com.cbs.oukasystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EntityScan("com.cbs.oukasystem.entity") // scan entity
@EnableJpaRepositories("com.cbs.oukasystem.repository") // scan repository
@SpringBootApplication
@EnableScheduling
public class OukaSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(OukaSystemApplication.class, args);
	}
}
