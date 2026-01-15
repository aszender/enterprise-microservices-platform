package com.aszender.orders.inventory;

import com.aszender.orders.model.OrderItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class InventoryClient {

    private final RestClient restClient;

    public InventoryClient(
            @Value("${app.inventory.base-url:http://localhost:8082}") String inventoryBaseUrl
    ) {
        this.restClient = RestClient.builder().baseUrl(inventoryBaseUrl).build();
    }

    public ReserveResponse reserve(Long orderId, List<OrderItem> items) {
        ReserveRequest req = new ReserveRequest(
                orderId,
                items.stream()
                        .map(i -> new ReserveLineRequest(i.getProductId(), i.getQuantity()))
                        .toList()
        );

        ReserveResponse response = restClient.post()
                .uri("/api/inventory/reservations")
                .body(req)
                .retrieve()
                .body(ReserveResponse.class);

        return (response == null)
                ? new ReserveResponse(false, "NO_RESPONSE")
                : response;
    }

    public record ReserveLineRequest(Long productId, Integer quantity) {
    }

    public record ReserveRequest(Long orderId, List<ReserveLineRequest> items) {
    }

    public record ReserveResponse(boolean reserved, String reason) {
    }
}
