package com.aszender.inventory.repository;

import com.aszender.inventory.model.StockReservation;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StockReservationRepository extends JpaRepository<StockReservation, Long> {
    Optional<StockReservation> findByOrderId(Long orderId);

    @EntityGraph(attributePaths = "items")
    @Query("select reservation from StockReservation reservation where reservation.orderId = :orderId")
    Optional<StockReservation> findByOrderIdWithItems(@Param("orderId") Long orderId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update StockReservation reservation
               set reservation.status = com.aszender.inventory.model.ReservationStatus.RELEASED
             where reservation.orderId = :orderId
               and reservation.status = com.aszender.inventory.model.ReservationStatus.RESERVED
            """)
    int markReleasedIfReserved(@Param("orderId") Long orderId);
}
