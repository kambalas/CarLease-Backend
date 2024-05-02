package com.example.sick;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication()
public class SickApplication {

	public static void main(String[] args) {
		SpringApplication.run(SickApplication.class, args);
	}

}
