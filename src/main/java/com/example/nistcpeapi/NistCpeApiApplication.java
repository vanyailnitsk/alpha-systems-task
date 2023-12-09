package com.example.nistcpeapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class NistCpeApiApplication {
	@Autowired
	private CpeService cpeService;
	public static void main(String[] args) {
		SpringApplication.run(NistCpeApiApplication.class, args);
	}
	@Bean
	CommandLineRunner commandLineRunner () {
		return args -> {
			cpeService.init();
		};
	}

}
