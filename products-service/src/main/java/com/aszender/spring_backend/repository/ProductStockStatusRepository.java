package com.aszender.spring_backend.repository;

import com.aszender.spring_backend.model.ProductStockStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductStockStatusRepository extends JpaRepository<ProductStockStatus, Long> {
}
