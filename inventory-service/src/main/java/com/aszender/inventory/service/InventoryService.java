package com.aszender.inventory.service;

import com.aszender.inventory.model.ReservationStatus;
import com.aszender.inventory.model.StockItem;
import com.aszender.inventory.model.StockReservation;
import com.aszender.inventory.repository.StockItemRepository;
import com.aszender.inventory.repository.StockReservationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class InventoryService {

    private final StockItemRepository stockItemRepository;
    private final StockReservationRepository stockReservationRepository;

    private final int defaultStock;

    public InventoryService(
            StockItemRepository stockItemRepository,
            StockReservationRepository stockReservationRepository,
            @Value("${app.inventory.default-stock:100}") int defaultStock
    ) {
        this.stockItemRepository = stockItemRepository;
        this.stockReservationRepository = stockReservationRepository;
        this.defaultStock = defaultStock;
    }

    @Transactional
    public StockItem ensureStockItemExists(Long productId) {
        return stockItemRepository.findByProductId(productId)
                .orElseGet(() -> stockItemRepository.save(new StockItem(productId, defaultStock)));
    }

    @Transactional
    public boolean reserveStock(Long orderId, java.util.List<ReservationLine> lines) {
        if (orderId == null) {
            throw new IllegalArgumentException("orderId is required");
        }
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("order lines are required");
        }

        Optional<StockReservation> existing = stockReservationRepository.findByOrderId(orderId);
        if (existing.isPresent()) {
            // Idempotency: if already reserved, treat as success; if released, do not re-reserve.
            return existing.get().getStatus() == ReservationStatus.RESERVED;
        }

        // Validate all stock exists and is sufficient BEFORE mutating.
        for (ReservationLine line : lines) {
            StockItem stockItem = stockItemRepository.findByProductId(line.productId())
                    .orElse(null);
            if (stockItem == null) {
                return false;
            }
            if (stockItem.getAvailable() < line.quantity()) {
                return false;
            }
        }

        // Apply reservations
        StockReservation reservation = new StockReservation(orderId);
        for (ReservationLine line : lines) {
            StockItem stockItem = stockItemRepository.findByProductId(line.productId())
                    .orElseThrow();
            stockItem.reserve(line.quantity());
            reservation.addItem(line.productId(), line.quantity());
        }

        stockReservationRepository.save(reservation);
        return true;
    }

    @Transactional
    public boolean releaseReservation(Long orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException("orderId is required");
        }

        StockReservation reservation = stockReservationRepository.findByOrderId(orderId)
                .orElse(null);
        if (reservation == null) {
            return false;
        }

        if (reservation.getStatus() == ReservationStatus.RELEASED) {
            return true;
        }

        reservation.getItems().forEach(item -> {
            StockItem stockItem = stockItemRepository.findByProductId(item.getProductId())
                    .orElseThrow();
            stockItem.releaseReserved(item.getQuantity());
        });

        reservation.release();
        return true;
    }

    public record ReservationLine(Long productId, int quantity) {
        public ReservationLine {
            if (productId == null) {
                throw new IllegalArgumentException("productId is required");
            }
            if (quantity <= 0) {
                throw new IllegalArgumentException("quantity must be > 0");
            }
        }
    }
}
