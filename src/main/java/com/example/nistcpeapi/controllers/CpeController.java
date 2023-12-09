package com.example.nistcpeapi.controllers;

import com.example.nistcpeapi.CpeService;
import com.example.nistcpeapi.models.CPE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
