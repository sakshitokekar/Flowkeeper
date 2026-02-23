package com.example.Flowkeeper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class SoundControllerTest {

    @Test
    void scheduleLastFiveBeeps_usesProvidedDuration() throws Exception {
        CapturingFlowkeeperService flowkeeperService = new CapturingFlowkeeperService();
        SoundController controller = new SoundController(flowkeeperService);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        mockMvc.perform(post("/api/sound/schedule-last-five").param("durationSeconds", "42"))
                .andExpect(status().isAccepted());

        assertEquals(42, flowkeeperService.lastDurationSeconds);
    }

    @Test
    void scheduleLastFiveBeeps_usesDefaultDurationWhenMissing() throws Exception {
        CapturingFlowkeeperService flowkeeperService = new CapturingFlowkeeperService();
        SoundController controller = new SoundController(flowkeeperService);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        mockMvc.perform(post("/api/sound/schedule-last-five"))
                .andExpect(status().isAccepted());

        assertEquals(300, flowkeeperService.lastDurationSeconds);
    }

    private static class CapturingFlowkeeperService extends FlowkeeperService {
        private volatile int lastDurationSeconds = -1;

        @Override
        public void scheduleLastFiveSecondBeeps(int durationSeconds) {
            lastDurationSeconds = durationSeconds;
        }
    }
}
