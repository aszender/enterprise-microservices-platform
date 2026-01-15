package com.aszender.spring_backend.controller;

import com.aszender.spring_backend.dto.PRequestDTO;
import com.aszender.spring_backend.dto.PResponseDTO;
import com.aszender.spring_backend.dto.StockStatusDTO;
import com.aszender.spring_backend.model.Product;
import com.aszender.spring_backend.model.ProductStockStatus;
import com.aszender.spring_backend.repository.ProductStockStatusRepository;
import com.aszender.spring_backend.service.ProductService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.net.URI;
import java.util.Map;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final ProductStockStatusRepository stockStatusRepository;

    public ProductController(ProductService productService, ProductStockStatusRepository stockStatusRepository) {
        this.productService = productService;
        this.stockStatusRepository = stockStatusRepository;
    }

    private static PResponseDTO toDto(Product product) {
        return new PResponseDTO(product.getId(), product.getName(), product.getDescription(), product.getPrice());
    }

    private static Product toEntity(PRequestDTO dto, Long id) {
        Product product = new Product(dto.name(), dto.description(), dto.price());
        product.setId(id);
        return product;
    }

    // GET all products
    @GetMapping
    public ResponseEntity<List<PResponseDTO>> getAllProducts(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        boolean hasPagination = page != null || size != null;
        boolean hasSortingOnly = !hasPagination && sortBy != null;

        if (hasPagination) {
            int pageValue = (page == null) ? 0 : page;
            int sizeValue = (size == null) ? 10 : size;
            String sortField = (sortBy == null || sortBy.isBlank()) ? "id" : sortBy;
            String dir = (direction == null || direction.isBlank()) ? "asc" : direction;

            if (pageValue < 0) {
                throw new IllegalArgumentException("page must be >= 0");
            }
            if (sizeValue <= 0) {
                throw new IllegalArgumentException("size must be > 0");
            }

            Sort sort = switch (dir.toLowerCase()) {
                case "asc" -> Sort.by(sortField).ascending();
                case "desc" -> Sort.by(sortField).descending();
                default -> throw new IllegalArgumentException("direction must be 'asc' or 'desc'");
            };

            List<PResponseDTO> products = productService.findAll(PageRequest.of(pageValue, sizeValue, sort))
                    .getContent()
                    .stream()
                    .map(ProductController::toDto)
                    .toList();

            return ResponseEntity.ok(products);
        }

        if (hasSortingOnly) {
            String dir = (direction == null || direction.isBlank()) ? "asc" : direction;
            Sort sort = switch (dir.toLowerCase()) {
                case "asc" -> Sort.by(sortBy).ascending();
                case "desc" -> Sort.by(sortBy).descending();
                default -> throw new IllegalArgumentException("direction must be 'asc' or 'desc'");
            };

            List<PResponseDTO> products = productService.findAll(sort).stream()
                    .map(ProductController::toDto)
                    .toList();

            return ResponseEntity.ok(products);
        }

        List<PResponseDTO> products = productService.findAll().stream()
                .map(ProductController::toDto)
                .toList();

        return ResponseEntity.ok(products);  // 200 OK
    }

    // GET single product
    @GetMapping("/{id}")
    public ResponseEntity<PResponseDTO> getProduct(@PathVariable Long id) {
        return productService.findById(id)
                .map(ProductController::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET product stock status (projection from inventory low-stock events)
    @GetMapping("/{id}/stock-status")
    public ResponseEntity<StockStatusDTO> getProductStockStatus(@PathVariable Long id) {
        if (!productService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        return stockStatusRepository.findById(id)
                .map(ProductController::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok(new StockStatusDTO(id, null, null, false, null)));
    }

    private static StockStatusDTO toDto(ProductStockStatus status) {
        return new StockStatusDTO(
                status.getProductId(),
                status.getAvailable(),
                status.getThreshold(),
                status.isLowStock(),
                status.getUpdatedAt()
        );
    }

    // POST - Create product
    @PostMapping
    public ResponseEntity<PResponseDTO> createProduct(@Valid @RequestBody PRequestDTO request) {
        Product saved = productService.save(toEntity(request, null));

        // Return 201 Created with Location header
        URI location = URI.create("/api/products/" + saved.getId());
        return ResponseEntity.created(location).body(toDto(saved));
    }

    // PUT - Full update
    @PutMapping("/{id}")
    public ResponseEntity<PResponseDTO> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody PRequestDTO request
    ) {
        if (!productService.existsById(id)) {
            return ResponseEntity.notFound().build();  // 404
        }

        Product updated = productService.save(toEntity(request, id));
        return ResponseEntity.ok(toDto(updated));  // 200 OK
    }

    // PATCH - Partial update
    @PatchMapping("/{id}")
    public ResponseEntity<PResponseDTO> patchProduct(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates
    ) {
        return productService.findById(id)
                .map(product -> {
                    // Apply partial updates
                    if (updates.containsKey("name")) {
                        product.setName((String) updates.get("name"));
                    }
                    if (updates.containsKey("description")) {
                        product.setDescription((String) updates.get("description"));
                    }
                    if (updates.containsKey("price")) {
                        Object rawPrice = updates.get("price");
                        if (rawPrice instanceof Number number) {
                            product.setPrice(number.doubleValue());
                        }
                    }

                    Product saved = productService.save(product);
                    return ResponseEntity.ok(toDto(saved));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (!productService.existsById(id)) {
            return ResponseEntity.notFound().build();  // 404
        }
        productService.deleteById(id);
        return ResponseEntity.noContent().build();  // 204 No Content
    }

    // SEARCH
    @GetMapping("/search")
    public ResponseEntity<List<PResponseDTO>> searchProducts(
            @RequestParam String keyword
    ) {
        List<PResponseDTO> results = productService.searchProducts(Objects.toString(keyword, "")).stream()
                .map(ProductController::toDto)
                .toList();

        return ResponseEntity.ok(results);
    }
}
