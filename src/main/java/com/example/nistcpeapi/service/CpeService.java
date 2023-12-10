package com.example.nistcpeapi.service;

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
import org.springframework.web.util.UriComponentsBuilder;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class CpeService {
    @Value("${api.key}")
    private String apiKey;
    private RestTemplate restTemplate;
    private final CpeRepository cpeRepository;
    @Autowired
    public CpeService(CpeRepository cpeRepository,RestTemplate restTemplate) {
        this.cpeRepository = cpeRepository;
        this.restTemplate = restTemplate;
    }
    public void createCpe(CPE cpe) {
        cpeRepository.save(cpe);
    }
    public void createCpe(List<CPE> cpeList) {
        cpeRepository.saveAllAndFlush(cpeList);
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
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                break;
            }
        }
        createCpe(cpeList);
        long timeDiff = System.currentTimeMillis()-start;
        System.out.println("Database completed in "+timeDiff/1000+" seconds");
    }
    public void dailyUpdate() {
        String uri =
                "https://services.nvd.nist.gov/rest/json/cpes/2.0/";
        HttpHeaders headers = new HttpHeaders();
        headers.set("apiKey", apiKey);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        Calendar calendar = Calendar.getInstance();
        Timestamp end = new Timestamp(calendar.getTimeInMillis());
        calendar.add(Calendar.DAY_OF_YEAR,-1);
        Timestamp start = new Timestamp(calendar.getTimeInMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        String formattedStart = sdf.format(new Date(start.getTime()));
        String formattedEnd = sdf.format(new Date(end.getTime()));
        String urlTemplate = UriComponentsBuilder.fromHttpUrl(uri)
                .queryParam("lastModStartDate", formattedStart)
                .queryParam("lastModEndDate",formattedEnd)
                .encode()
                .toUriString();
        ResultSet resultSet = restTemplate.exchange(
                urlTemplate,
                HttpMethod.GET,
                entity,
                ResultSet.class
        ).getBody();
        cpeRepository.saveAll(resultSet.getProducts());
        log.info("Database updated successfully! {} rows updated ",resultSet.getTotalResults());
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
