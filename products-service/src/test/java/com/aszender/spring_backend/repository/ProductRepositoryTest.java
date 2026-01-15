package com.aszender.spring_backend.repository;

import com.aszender.spring_backend.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
        Product p1 = new Product("Laptop", "Work machine", 1200.0);
        Product p2 = new Product("Mouse", "Wireless", 25.0);
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
        Product p1 = new Product("Camera", "DSLR", 500.0);
        Product p2 = new Product("Phone", "Android", 800.0);
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
