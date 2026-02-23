package com.example.Flowkeeper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

class FlowkeeperServiceTest {

    @Test
    void scheduleLastFiveSecondBeeps_clampsToAtLeastOneBeep() throws InterruptedException {
        TestableFlowkeeperService service = new TestableFlowkeeperService(1);
        try {
            service.scheduleLastFiveSecondBeeps(0);
            assertTrue(service.awaitBeeps(2), "Expected at least one beep for non-positive duration");
            assertEquals(1, service.beepCount());
        } finally {
            service.shutdownBeepExecutor();
        }
    }

    @Test
    void scheduleLastFiveSecondBeeps_usesDurationWhenLessThanFive() throws InterruptedException {
        TestableFlowkeeperService service = new TestableFlowkeeperService(3);
        try {
            service.scheduleLastFiveSecondBeeps(3);
            assertTrue(service.awaitBeeps(4), "Expected exactly three beeps for duration=3");
            assertEquals(3, service.beepCount());
        } finally {
            service.shutdownBeepExecutor();
        }
    }

    private static class TestableFlowkeeperService extends FlowkeeperService {
        private final CountDownLatch latch;
        private final AtomicInteger beeps = new AtomicInteger();

        private TestableFlowkeeperService(int expectedBeeps) {
            this.latch = new CountDownLatch(expectedBeeps);
        }

        @Override
        public void playSystemBeep() {
            beeps.incrementAndGet();
            latch.countDown();
        }

        private boolean awaitBeeps(int timeoutSeconds) throws InterruptedException {
            return latch.await(timeoutSeconds, TimeUnit.SECONDS);
        }

        private int beepCount() {
            return beeps.get();
        }
    }
}
