package com.aszender.inventory.service;

import com.aszender.inventory.model.ReservationStatus;
import com.aszender.inventory.model.StockItem;
import com.aszender.inventory.model.StockReservation;
import com.aszender.inventory.repository.StockItemRepository;
import com.aszender.inventory.repository.StockReservationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;
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
    public boolean reserveStock(Long orderId, List<ReservationLine> lines) {
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

        StockReservation reservation = new StockReservation(orderId);
        for (ReservationLine line : lines) {
            int updated = stockItemRepository.reserveAvailable(line.productId(), line.quantity());
            if (updated != 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }
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

        List<ReservationLine> reservedLines = reservation.getItems().stream()
                .map(item -> new ReservationLine(item.getProductId(), item.getQuantity()))
                .toList();

        int markedReleased = stockReservationRepository.markReleasedIfReserved(orderId);
        if (markedReleased != 1) {
            return stockReservationRepository.findByOrderId(orderId)
                    .map(existing -> existing.getStatus() == ReservationStatus.RELEASED)
                    .orElse(false);
        }

        for (ReservationLine line : reservedLines) {
            int updated = stockItemRepository.releaseReserved(line.productId(), line.quantity());
            if (updated != 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }
        }
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
