package com.example.nistcpeapi;

import com.example.nistcpeapi.config.AppConfig;
import com.example.nistcpeapi.models.CPE;
import com.example.nistcpeapi.models.Product;
import com.example.nistcpeapi.models.ResultSet;
import com.example.nistcpeapi.repositories.CpeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CpeService {
    @Value("${api.key}")
    private String apiKey;
    @Autowired
    private RestTemplate restTemplate;
    private final CpeRepository cpeRepository;

    @Autowired
    public CpeService(CpeRepository cpeRepository) {
        this.cpeRepository = cpeRepository;
    }
    public void createCpe(CPE cpe) {
        cpeRepository.save(cpe);
    }
    public void createCpe(List<CPE> cpeList) {
        cpeRepository.saveAll(cpeList);
    }
    public void init() throws InterruptedException {
        String uri = "https://services.nvd.nist.gov/rest/json/cpes/2.0?resultsPerPage=10000&startIndex={index}";
        HttpHeaders headers = new HttpHeaders();
        headers.set("apiKey", apiKey);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        int totalResults = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                entity,
                ResultSet.class,
                Collections.singletonMap("index", 0)
        ).getBody().getTotalResults();
        List<CPE> cpeList = new ArrayList<>();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i+=10000) {
            try {
                ResponseEntity<ResultSet> response = restTemplate.exchange(
                        uri,
                        HttpMethod.GET,
                        entity,
                        ResultSet.class,
                        Collections.singletonMap("index", i)
                );
                ResultSet resultSet = response.getBody();
                cpeList.addAll(resultSet.getProducts());
                System.out.println(cpeList.size()+"/"+totalResults+" rows readed");
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                break;
            }
        }
        createCpe(cpeList);
        long timeDiff = System.currentTimeMillis()-start;
        System.out.println("Database completed in "+timeDiff/1000+" seconds");
    }
}
