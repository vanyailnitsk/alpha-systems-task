package com.example.nistcpeapi.config;

import com.example.nistcpeapi.service.CpeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
//    @Autowired
//    private CpeService cpeService;
    @Autowired
    private RestTemplateBuilder restTemplateBuilder;
    @Bean(name = "restTemplate")
    public RestTemplate restTemplate() {
        return restTemplateBuilder
           .build();
    }

    @Bean
    CommandLineRunner commandLineRunner (CpeService cpeService) {
        return args -> {
            cpeService.init();
        };
    }
}
