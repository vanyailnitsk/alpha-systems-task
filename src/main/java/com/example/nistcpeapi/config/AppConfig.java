package com.example.nistcpeapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
    @Autowired
    private RestTemplateBuilder restTemplateBuilder;
    @Bean(name = "restTemplate")
    public RestTemplate restTemplate() {
        return restTemplateBuilder
//                .setConnectTimeout(Duration.ofSeconds(1))
//           .setReadTimeout(Duration.ofSeconds(1))
           .build();
    }
}
