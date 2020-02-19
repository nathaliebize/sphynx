package com.nathaliebize.sphynx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/***
 * This is the starting point for Sphynx.
 */
@SpringBootApplication
@EnableJpaAuditing
public class SphynxApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
	    SpringApplication.run(SphynxApplication.class, args);	    
	}

}
