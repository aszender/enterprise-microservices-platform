package com.aszender.spring_backend.service;

import com.aszender.spring_backend.exception.ProductNotFoundException;
import com.aszender.spring_backend.model.Product;
import com.aszender.spring_backend.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void updateProduct_updatesFieldsAndSaves() {
        // Arrange
        Long id = 10L;
        Product existing = new Product("Old", "Old desc", 1.0);
        existing.setId(id);

        Product update = new Product("New", "New desc", 99.99);

        when(productRepository.findById(id)).thenReturn(Optional.of(existing));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Product result = productService.updateProduct(id, update);

        // Assert
        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());
        Product saved = captor.getValue();

        assertThat(saved.getId()).isEqualTo(id);
        assertThat(saved.getName()).isEqualTo("New");
        assertThat(saved.getDescription()).isEqualTo("New desc");
        assertThat(saved.getPrice()).isEqualTo(99.99);

        assertThat(result.getName()).isEqualTo("New");
        verify(productRepository).findById(id);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void updateProduct_whenNotFound_throwsProductNotFoundException() {
        // Arrange
        Long id = 999L;
        Product update = new Product("New", "New desc", 99.99);
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> productService.updateProduct(id, update))
                .isInstanceOf(ProductNotFoundException.class);

        verify(productRepository).findById(id);
        verifyNoMoreInteractions(productRepository);
    }
}
