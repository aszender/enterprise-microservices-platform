package com.aszender.orders.inventory;

import com.aszender.contracts.inventory.v1.InventoryServiceGrpc;
import com.aszender.contracts.inventory.v1.ReserveItem;
import com.aszender.contracts.inventory.v1.ReserveStockRequest;
import com.aszender.contracts.inventory.v1.ReserveStockResponse;
import com.aszender.orders.model.OrderItem;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GrpcInventoryClient {

    private final ManagedChannel channel;
    private final InventoryServiceGrpc.InventoryServiceBlockingStub stub;

    public GrpcInventoryClient(
            @Value("${app.grpc.inventory.host:localhost}") String host,
            @Value("${app.grpc.inventory.port:9090}") int port
    ) {
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                // Learning repo: plaintext local gRPC. In real prod, you’d use TLS + auth.
                .usePlaintext()
                .build();
        this.stub = InventoryServiceGrpc.newBlockingStub(channel);
    }

    public ReserveResponse reserve(Long orderId, List<OrderItem> items) {
        ReserveStockRequest req = ReserveStockRequest.newBuilder()
                .setOrderId(orderId == null ? 0 : orderId)
                .addAllItems(items.stream()
                        .map(i -> ReserveItem.newBuilder()
                                .setProductId(i.getProductId())
                                .setQuantity(i.getQuantity())
                                .build())
                        .toList())
                .build();

        ReserveStockResponse res = stub.reserveStock(req);
        String reason = res.getReason();
        if (reason != null && reason.isBlank()) {
            reason = null;
        }
        return new ReserveResponse(res.getReserved(), reason);
    }

    public void shutdown() {
        channel.shutdown();
    }

    @PreDestroy
    public void preDestroy() {
        shutdown();
    }

    public record ReserveResponse(boolean reserved, String reason) {
    }
}
