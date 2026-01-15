package com.aszender.spring_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "product_stock_status")
public class ProductStockStatus {

    @Id
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(nullable = false)
    private int available;

    @Column(nullable = false)
    private int threshold;

    @Column(name = "low_stock", nullable = false)
    private boolean lowStock;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected ProductStockStatus() {
    }

    public ProductStockStatus(Long productId, int available, int threshold, boolean lowStock, Instant updatedAt) {
        this.productId = productId;
        this.available = available;
        this.threshold = threshold;
        this.lowStock = lowStock;
        this.updatedAt = updatedAt;
    }

    public Long getProductId() {
        return productId;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public boolean isLowStock() {
        return lowStock;
    }

    public void setLowStock(boolean lowStock) {
        this.lowStock = lowStock;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
