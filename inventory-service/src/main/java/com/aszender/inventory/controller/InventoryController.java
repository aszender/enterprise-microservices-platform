package com.aszender.inventory.controller;

import com.aszender.inventory.model.StockItem;
import com.aszender.inventory.model.StockReservation;
import com.aszender.inventory.repository.StockItemRepository;
import com.aszender.inventory.repository.StockReservationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final StockItemRepository stockItemRepository;
    private final StockReservationRepository stockReservationRepository;

    public InventoryController(StockItemRepository stockItemRepository, StockReservationRepository stockReservationRepository) {
        this.stockItemRepository = stockItemRepository;
        this.stockReservationRepository = stockReservationRepository;
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
}
