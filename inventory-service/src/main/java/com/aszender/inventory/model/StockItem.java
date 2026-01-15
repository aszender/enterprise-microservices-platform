package com.aszender.inventory.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "stock_items", uniqueConstraints = {
        @UniqueConstraint(name = "uk_stock_items_product_id", columnNames = "product_id")
})
public class StockItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(nullable = false)
    private int available;

    @Column(nullable = false)
    private int reserved;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    protected StockItem() {
    }

    public StockItem(Long productId, int available) {
        this.productId = productId;
        this.available = available;
        this.reserved = 0;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public int getAvailable() {
        return available;
    }

    public int getReserved() {
        return reserved;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void reserve(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be > 0");
        }
        if (available < quantity) {
            throw new IllegalArgumentException("insufficient stock for productId=" + productId);
        }
        available -= quantity;
        reserved += quantity;
    }

    public void releaseReserved(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be > 0");
        }
        if (reserved < quantity) {
            throw new IllegalArgumentException("cannot release more than reserved for productId=" + productId);
        }
        reserved -= quantity;
        available += quantity;
    }
}
