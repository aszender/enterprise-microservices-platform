package com.aszender.inventory.grpc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcServerConfig {

    @Bean
    public GrpcServerLifecycle grpcServerLifecycle(
            @Value("${app.grpc.server.port:9090}") int port,
            InventoryGrpcService inventoryGrpcService
    ) {
        return new GrpcServerLifecycle(port, inventoryGrpcService);
    }
}
