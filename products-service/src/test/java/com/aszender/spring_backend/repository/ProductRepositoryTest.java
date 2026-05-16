package com.aszender.spring_backend.repository;

import com.aszender.spring_backend.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void findByNameContainingIgnoreCase_returnsMatchingProducts() {
        // Arrange
        Product p1 = new Product("Laptop", "Work machine", BigDecimal.valueOf(1200.00));
        Product p2 = new Product("Mouse", "Wireless", BigDecimal.valueOf(25.00));
        productRepository.saveAll(List.of(p1, p2));

        // Act
        List<Product> results = productRepository.findByNameContainingIgnoreCase("lap");

        // Assert
        assertThat(results)
            .extracting(Product::getName)
            .contains("Laptop");
    }

    @Test
    void findbyaLetter_returnsMatchingProducts() {
        // Arrange
        Product p1 = new Product("Camera", "DSLR", BigDecimal.valueOf(500.00));
        Product p2 = new Product("Phone", "Android", BigDecimal.valueOf(800.00));
        productRepository.saveAll(List.of(p1, p2));

        // Act
        List<Product> results = productRepository.findbyaLetter("am");

        // Assert
        assertThat(results)
                .extracting(Product::getName)
                .contains("Camera")
                .doesNotContain("Phone");
    }
}
