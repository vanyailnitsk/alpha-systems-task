package com.example.nistcpeapi;

import com.example.nistcpeapi.service.CpeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NistCpeApiApplication {
	@Autowired
	private CpeService cpeService;
	public static void main(String[] args) {
		SpringApplication.run(NistCpeApiApplication.class, args);
	}


}
