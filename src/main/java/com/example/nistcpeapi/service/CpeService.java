package com.example.nistcpeapi.service;

import com.example.nistcpeapi.models.CPE;
import com.example.nistcpeapi.repositories.CpeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CpeService {
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
}
