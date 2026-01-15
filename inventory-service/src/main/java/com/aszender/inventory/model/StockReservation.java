package com.aszender.inventory.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stock_reservations", uniqueConstraints = {
        @UniqueConstraint(name = "uk_stock_reservations_order_id", columnNames = "order_id")
})
public class StockReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status = ReservationStatus.RESERVED;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockReservationItem> items = new ArrayList<>();

    protected StockReservation() {
    }

    public StockReservation(Long orderId) {
        this.orderId = orderId;
        this.status = ReservationStatus.RESERVED;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public List<StockReservationItem> getItems() {
        return items;
    }

    public void addItem(Long productId, int quantity) {
        StockReservationItem item = new StockReservationItem(this, productId, quantity);
        this.items.add(item);
    }

    public void release() {
        this.status = ReservationStatus.RELEASED;
    }
}
