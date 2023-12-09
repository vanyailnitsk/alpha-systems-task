package com.example.nistcpeapi;

import com.example.nistcpeapi.config.AppConfig;
import com.example.nistcpeapi.models.Product;
import com.example.nistcpeapi.models.ResultSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@ExtendWith(MockitoExtension.class)
public class CpeTest {
    @Value("${api.key}")
    private String apiKey;
    @Test
    void init() throws InterruptedException {
        String uri = "https://services.nvd.nist.gov/rest/json/cpes/2.0?startIndex=10";
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        RestTemplate restTemplate = (RestTemplate) context.getBean("restTemplate");

        HttpHeaders headers = new HttpHeaders();
        headers.set("apiKey", "2a7b7579-7156-4703-9a64-839a90a5b6fd");

        int startIndex = 10000;
        int resultsPerPage = 10;
        for (int i = 0; i < 1000; i++) {
            try {
                ResponseEntity<ResultSet> response = restTemplate.getForEntity(
                        uri,
                        ResultSet.class,
                        headers
                );
                ResultSet resultSet = response.getBody();
                System.out.println(i);

            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
}
