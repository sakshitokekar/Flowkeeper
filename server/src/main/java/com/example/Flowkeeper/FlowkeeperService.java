package com.example.Flowkeeper;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.net.URI;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.annotation.PreDestroy;

@Service
public class FlowkeeperService {

    private final ScheduledExecutorService beepExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r, "flowkeeper-beep-executor");
        t.setDaemon(true);
        return t;
    });

    @Scheduled(fixedRate = 60 * 60 * 1000, initialDelay = 5 * 1000)
    public void openBreakPage() {
        System.setProperty("java.awt.headless", "false");
        try {
            URI uri = new URI("http://localhost:8080/break.html");
            Desktop.getDesktop().browse(uri);
            System.out.println("[TakeBreak] Break page opened: " + uri);
        } catch (Exception e) {
            System.out.println("[TakeBreak] Error opening break page: " + e.getMessage());
        }
    }

    public void scheduleLastFiveSecondBeeps(int durationSeconds) {
        int safeDuration = Math.max(durationSeconds, 1);
        int beepCount = Math.min(5, safeDuration);
        int firstDelay = Math.max(0, safeDuration - beepCount);

        for (int i = 0; i < beepCount; i++) {
            final int secondsRemaining = beepCount - i;
            int delaySeconds = firstDelay + i;
            beepExecutor.schedule(() -> {
                playSystemBeep();
                System.out.println("[TakeBreak] Beep: " + secondsRemaining + "s remaining");
            }, delaySeconds, TimeUnit.SECONDS);
        }
    }

    public void playSystemBeep() {
        try {
            System.setProperty("java.awt.headless", "false");
            Toolkit.getDefaultToolkit().beep();
        } catch (Exception e) {
            // terminal bell fallback
            System.out.print("\007");
        }
    }

    @PreDestroy
    public void shutdownBeepExecutor() {
        beepExecutor.shutdownNow();
    }
}
