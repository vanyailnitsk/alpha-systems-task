package com.example.nistcpeapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {
    private final CpeService cpeService;

    @Autowired
    public ScheduledTask(CpeService cpeService) {
        this.cpeService = cpeService;
    }

    @Scheduled(cron = "0 00 13 * * *")
    public void executeDailyTask() {
        cpeService.dailyUpdate();
    }
}
