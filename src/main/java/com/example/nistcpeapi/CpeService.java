package com.example.nistcpeapi;

import com.example.nistcpeapi.models.CPE;
import com.example.nistcpeapi.models.ResultSet;
import com.example.nistcpeapi.repositories.CpeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
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
    public void init() {
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
                break;
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                break;
            }
        }
        createCpe(cpeList);
        long timeDiff = System.currentTimeMillis()-start;
        System.out.println("Database completed in "+timeDiff/1000+" seconds");
    }

    public CPE getById(UUID cpeNameId) {
        UUID lowerCase = UUID.fromString(cpeNameId.toString().toLowerCase());
        return cpeRepository.findById(lowerCase).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public List<CPE> getCpeListByIds(List<UUID> ids) {
        return cpeRepository.findAllById(ids);
    }

    public List<CPE> getCpeListByNames(List<String> names) {
        return cpeRepository.findAllByCpeNameIn(names);
    }

    public Page<CPE> getCpesByCpeNameAndDescription(String cpeName, String description, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return cpeRepository.findByCpeNameAndDescription(cpeName, description, pageRequest);
    }
}
