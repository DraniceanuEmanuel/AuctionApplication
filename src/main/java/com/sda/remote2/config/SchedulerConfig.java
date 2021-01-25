package com.sda.remote2.config;

import com.sda.remote2.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    private BidService bidService;

    @Autowired
    public SchedulerConfig(BidService bidService) {
        this.bidService = bidService;
    }

    @Scheduled(fixedDelay = 50000)
    public void cronJob() {
        System.out.println("Running cronJob at " + LocalDateTime.now());
        bidService.assignWinners();
    }

}
