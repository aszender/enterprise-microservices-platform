package com.aszender.inventory;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
    "app.grpc.server.enabled=false"
})
class InventoryServiceApplicationTests {

    @Test
    void contextLoads() {
    }
}
