package com.example.nistcpeapi.controllers;

import com.example.nistcpeapi.service.CpeService;
import com.example.nistcpeapi.models.CPE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cpe")
public class CpeController {
    private final CpeService cpeService;

    @Autowired
    public CpeController(CpeService cpeService) {
        this.cpeService = cpeService;
    }
    @GetMapping("")
    public CPE getById(@RequestParam UUID cpeNameId) {
        return cpeService.getById(cpeNameId);
    }
    @GetMapping("/list-by-id")
    public List<CPE> getCpeListByIds(@RequestParam List<UUID> ids) {
        if (ids != null && !ids.isEmpty()) {
            return cpeService.getCpeListByIds(ids);
        } else {
            return List.of();
        }
    }
    @GetMapping("/list-by-name")
    public List<CPE> getCpeListByNames(@RequestParam List<String> names) {
        if (names != null && !names.isEmpty()) {
            return cpeService.getCpeListByNames(names);
        } else {
            return List.of();
        }
    }

    @GetMapping("/search")
    public Page<CPE> searchCpesByCpeNameAndDescription(@RequestParam(required = false,defaultValue = "") String cpeName,
                                                       @RequestParam(required = false,defaultValue = "") String description,
                                                       @RequestParam int page,
                                                       @RequestParam int size) {
        Page<CPE> result = cpeService.getCpesByCpeNameAndDescription(cpeName, description, page, size);
        return result;
    }
}
