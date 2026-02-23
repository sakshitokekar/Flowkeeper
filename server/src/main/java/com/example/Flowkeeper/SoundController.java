package com.example.Flowkeeper;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sound")
public class SoundController {

    private final FlowkeeperService flowkeeperService;

    public SoundController(FlowkeeperService flowkeeperService) {
        this.flowkeeperService = flowkeeperService;
    }

    @PostMapping("/schedule-last-five")
    public ResponseEntity<Void> scheduleLastFiveBeeps(
            @RequestParam(name = "durationSeconds", defaultValue = "300") int durationSeconds) {
        flowkeeperService.scheduleLastFiveSecondBeeps(durationSeconds);
        return ResponseEntity.accepted().build();
    }
}
