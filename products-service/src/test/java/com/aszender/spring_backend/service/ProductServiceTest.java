package com.aszender.spring_backend.service;

import com.aszender.spring_backend.exception.ProductNotFoundException;
import com.aszender.spring_backend.kafka.outbox.service.ProductOutboxService;
import com.aszender.spring_backend.model.Product;
import com.aszender.spring_backend.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductOutboxService productOutboxService;

    @InjectMocks
    private ProductService productService;

    @Test
    void save_newProduct_enqueuesToOutbox() {
        Product product = new Product("Widget", "A widget", BigDecimal.valueOf(9.99));
        Product saved = new Product("Widget", "A widget", BigDecimal.valueOf(9.99));
        saved.setId(1L);

        when(productRepository.save(product)).thenReturn(saved);

        Product result = productService.save(product);

        assertThat(result.getId()).isEqualTo(1L);
        verify(productOutboxService).enqueueProductCreated(saved);
    }

    @Test
    void save_existingProduct_doesNotEnqueueOutbox() {
        Product product = new Product("Widget", "A widget", BigDecimal.valueOf(9.99));
        product.setId(42L);

        when(productRepository.save(product)).thenReturn(product);

        productService.save(product);

        verifyNoInteractions(productOutboxService);
    }

    @Test
    void updateProduct_updatesFieldsAndSaves() {
        Long id = 10L;
        Product existing = new Product("Old", "Old desc", BigDecimal.valueOf(1.00));
        existing.setId(id);

        Product update = new Product("New", "New desc", BigDecimal.valueOf(99.99));

        when(productRepository.findById(id)).thenReturn(Optional.of(existing));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        Product result = productService.updateProduct(id, update);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());
        Product saved = captor.getValue();

        assertThat(saved.getId()).isEqualTo(id);
        assertThat(saved.getName()).isEqualTo("New");
        assertThat(saved.getDescription()).isEqualTo("New desc");
        assertThat(saved.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(99.99));
        assertThat(result.getName()).isEqualTo("New");

        verify(productRepository).findById(id);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void updateProduct_whenNotFound_throwsProductNotFoundException() {
        Long id = 999L;
        Product update = new Product("New", "New desc", BigDecimal.valueOf(99.99));
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.updateProduct(id, update))
                .isInstanceOf(ProductNotFoundException.class);

        verify(productRepository).findById(id);
        verifyNoMoreInteractions(productRepository);
    }
}
