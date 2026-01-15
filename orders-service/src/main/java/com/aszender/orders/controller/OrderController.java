package com.aszender.orders.controller;

import com.aszender.orders.dto.CreateOrderRequest;
import com.aszender.orders.dto.OrderItemResponse;
import com.aszender.orders.dto.OrderResponse;
import com.aszender.orders.model.Order;
import com.aszender.orders.model.OrderStatus;
import com.aszender.orders.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    private static OrderResponse toDto(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(i -> new OrderItemResponse(i.getId(), i.getProductId(), i.getQuantity(), i.getUnitPrice()))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getCustomerName(),
                order.getStatus(),
                order.getTotal(),
                order.getCreatedAt(),
                items
        );
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> listOrders() {
        List<OrderResponse> orders = orderService.findAll().stream()
                .map(OrderController::toDto)
                .toList();

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(toDto(orderService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        Order saved = orderService.create(request);
        URI location = URI.create("/api/orders/" + saved.getId());
        return ResponseEntity.created(location).body(toDto(saved));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status
    ) {
        Order updated = orderService.updateStatus(id, status);
        return ResponseEntity.ok(toDto(updated));
    }

    @PostMapping("/{id}/reserve")
    public ResponseEntity<OrderResponse> reserveStock(@PathVariable Long id) {
        Order updated = orderService.reserveStock(id);
        return ResponseEntity.ok(toDto(updated));
    }
}
