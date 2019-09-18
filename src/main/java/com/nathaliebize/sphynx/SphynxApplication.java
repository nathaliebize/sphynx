package com.nathaliebize.sphynx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SphynxApplication {

	public static void main(String[] args) {
	    SpringApplication.run(SphynxApplication.class, args);
	}

}
