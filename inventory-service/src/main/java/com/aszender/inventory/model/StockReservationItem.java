package com.aszender.inventory.model;

import jakarta.persistence.*;

@Entity
@Table(name = "stock_reservation_items")
public class StockReservationItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private StockReservation reservation;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(nullable = false)
    private int quantity;

    protected StockReservationItem() {
    }

    public StockReservationItem(StockReservation reservation, Long productId, int quantity) {
        this.reservation = reservation;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public StockReservation getReservation() {
        return reservation;
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
