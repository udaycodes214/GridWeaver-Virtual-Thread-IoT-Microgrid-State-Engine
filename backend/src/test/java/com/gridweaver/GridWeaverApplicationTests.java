package com.gridweaver;

import static org.assertj.core.api.Assertions.assertThat;

import com.gridweaver.model.BatteryState;
import com.gridweaver.model.IoTNode;
import com.gridweaver.service.NodeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GridWeaverApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NodeService nodeService;

    @Test
    void contextLoads() {
        assertThat(nodeService).isNotNull();
    }

    @Test
    void healthEndpointReturnsOk() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk());
    }

    @Test
    void nodeCanBeCreated() {
        IoTNode node = new IoTNode("SOLAR-0001", 28.5, 77.4, 72, 6.5, 3.2, BatteryState.CHARGING, "ZONE-A");
        assertThat(node.getNodeId()).isEqualTo("SOLAR-0001");
        assertThat(node.getState()).isEqualTo(BatteryState.CHARGING);
    }
}
