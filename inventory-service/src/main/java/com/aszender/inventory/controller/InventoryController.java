package com.aszender.inventory.controller;

import com.aszender.inventory.kafka.events.OrderItemEvent;
import com.aszender.inventory.kafka.events.StockReservationFailedEvent;
import com.aszender.inventory.kafka.events.StockReleasedEvent;
import com.aszender.inventory.kafka.events.StockReservedEvent;
import com.aszender.inventory.kafka.publish.StockEventsPublisher;
import com.aszender.inventory.model.StockItem;
import com.aszender.inventory.model.StockReservation;
import com.aszender.inventory.repository.StockItemRepository;
import com.aszender.inventory.repository.StockReservationRepository;
import com.aszender.inventory.service.InventoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final StockItemRepository stockItemRepository;
    private final StockReservationRepository stockReservationRepository;
    private final InventoryService inventoryService;
    private final StockEventsPublisher stockEventsPublisher;

    public InventoryController(
            StockItemRepository stockItemRepository,
            StockReservationRepository stockReservationRepository,
            InventoryService inventoryService,
            StockEventsPublisher stockEventsPublisher
    ) {
        this.stockItemRepository = stockItemRepository;
        this.stockReservationRepository = stockReservationRepository;
        this.inventoryService = inventoryService;
        this.stockEventsPublisher = stockEventsPublisher;
    }

    @GetMapping("/stock")
    public ResponseEntity<List<StockItem>> listStock() {
        return ResponseEntity.ok(stockItemRepository.findAll());
    }

    @GetMapping("/stock/{productId}")
    public ResponseEntity<StockItem> getStock(@PathVariable Long productId) {
        return stockItemRepository.findByProductId(productId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/reservations/{orderId}")
    public ResponseEntity<StockReservation> getReservation(@PathVariable Long orderId) {
        return stockReservationRepository.findByOrderId(orderId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

        public record ReserveLineRequest(
            @NotNull Long productId,
            @NotNull @Min(1) Integer quantity
        ) {
        }

        public record ReserveRequest(
            @NotNull Long orderId,
            @NotEmpty List<@Valid ReserveLineRequest> items
        ) {
        }

        public record ReserveResponse(
            boolean reserved,
            String reason
        ) {
        }

        @PostMapping("/reservations")
        public ResponseEntity<ReserveResponse> reserve(@Valid @RequestBody ReserveRequest request) {
        boolean reserved = inventoryService.reserveStock(
            request.orderId(),
            request.items().stream()
                .map(i -> new InventoryService.ReservationLine(i.productId(), i.quantity()))
                .toList()
        );

        if (reserved) {
            stockEventsPublisher.publishStockReserved(new StockReservedEvent(
                request.orderId(),
                Instant.now(),
                request.items().stream()
                    .map(i -> new OrderItemEvent(i.productId(), i.quantity()))
                    .toList()
            ));

            return ResponseEntity.ok(new ReserveResponse(true, null));
        }

        stockEventsPublisher.publishStockReservationFailed(new StockReservationFailedEvent(
            request.orderId(),
            Instant.now(),
            "INSUFFICIENT_STOCK"
        ));

        return ResponseEntity.ok(new ReserveResponse(false, "INSUFFICIENT_STOCK"));
        }

        public record ReleaseResponse(
            boolean released
        ) {
        }

        @DeleteMapping("/reservations/{orderId}")
        public ResponseEntity<ReleaseResponse> release(@PathVariable Long orderId) {
        boolean released = inventoryService.releaseReservation(orderId);
        if (released) {
            stockEventsPublisher.publishStockReleased(new StockReleasedEvent(orderId, Instant.now()));
        }
        return ResponseEntity.ok(new ReleaseResponse(released));
        }
}
