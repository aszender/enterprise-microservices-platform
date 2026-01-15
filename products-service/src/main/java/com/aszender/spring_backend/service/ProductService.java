package com.aszender.spring_backend.service;

import com.aszender.spring_backend.exception.ProductNotFoundException;
import com.aszender.spring_backend.kafka.publish.ProductEventsPublisher;
import com.aszender.spring_backend.model.Product;
import com.aszender.spring_backend.repository.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductEventsPublisher productEventsPublisher;

    public ProductService(ProductRepository productRepository, ProductEventsPublisher productEventsPublisher) {
        this.productRepository = productRepository;
        this.productEventsPublisher = productEventsPublisher;
    }

    @Cacheable(cacheNames = "productsAll", key = "'all'")
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> findAll(Sort sort) {
        return productRepository.findAll(sort);
    }

    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Cacheable(cacheNames = "productsById", key = "#id", unless = "#result == null || #result.isEmpty()")
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Caching(
            put = @CachePut(cacheNames = "productsById", key = "#result.id", unless = "#result == null || #result.id == null"),
            evict = {
                    @CacheEvict(cacheNames = "productsAll", allEntries = true),
                    @CacheEvict(cacheNames = "productsSearch", allEntries = true)
            }
    )
    public Product save(Product product) {
        boolean isNew = product.getId() == null;
        Product saved = productRepository.save(product);

        if (isNew) {
            // Publish only after the transaction commits, so we don't emit events for rolled-back writes.
            if (TransactionSynchronizationManager.isActualTransactionActive()) {
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        productEventsPublisher.publishProductCreated(saved);
                    }
                });
            } else {
                productEventsPublisher.publishProductCreated(saved);
            }
        }

        return saved;
    }

    public boolean existsById(Long id) {
        return productRepository.existsById(id);
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "productsById", key = "#id"),
            @CacheEvict(cacheNames = "productsAll", allEntries = true),
            @CacheEvict(cacheNames = "productsSearch", allEntries = true)
    })
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> getAllProducts() {
        return findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return findById(id);
    }

    public Product createProduct(Product product) {
        return save(product);
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());

        return save(product);
    }

    public void deleteProduct(Long id) {
        deleteById(id);
    }

    @Cacheable(cacheNames = "productsSearch", key = "#keyword == null ? '' : #keyword.toLowerCase()")
    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }
}
