package com.aszender.inventory.repository;

import com.aszender.inventory.model.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StockItemRepository extends JpaRepository<StockItem, Long> {
    Optional<StockItem> findByProductId(Long productId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update StockItem stockItem
               set stockItem.available = stockItem.available - :quantity,
                   stockItem.reserved = stockItem.reserved + :quantity,
                   stockItem.version = stockItem.version + 1
             where stockItem.productId = :productId
               and stockItem.available >= :quantity
            """)
    int reserveAvailable(@Param("productId") Long productId, @Param("quantity") int quantity);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update StockItem stockItem
               set stockItem.available = stockItem.available + :quantity,
                   stockItem.reserved = stockItem.reserved - :quantity,
                   stockItem.version = stockItem.version + 1
             where stockItem.productId = :productId
               and stockItem.reserved >= :quantity
            """)
    int releaseReserved(@Param("productId") Long productId, @Param("quantity") int quantity);
}
