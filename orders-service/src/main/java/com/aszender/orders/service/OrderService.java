package com.aszender.orders.service;

import com.aszender.orders.dto.CreateOrderRequest;
import com.aszender.orders.exception.OrderNotFoundException;
import com.aszender.orders.inventory.InventoryClient;
import com.aszender.orders.kafka.publish.OrderEventsPublisher;
import com.aszender.orders.model.Order;
import com.aszender.orders.model.OrderItem;
import com.aszender.orders.model.OrderStatus;
import com.aszender.orders.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderEventsPublisher orderEventsPublisher;
    private final InventoryClient inventoryClient;

    public OrderService(
            OrderRepository orderRepository,
            OrderEventsPublisher orderEventsPublisher,
            InventoryClient inventoryClient
    ) {
        this.orderRepository = orderRepository;
        this.orderEventsPublisher = orderEventsPublisher;
        this.inventoryClient = inventoryClient;
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Transactional
    public Order create(CreateOrderRequest request) {
        Order order = new Order(request.customerName());

        double total = 0.0;
        for (var itemReq : request.items()) {
            if (itemReq.quantity() == null || itemReq.quantity() <= 0) {
                throw new IllegalArgumentException("quantity must be >= 1");
            }
            if (itemReq.unitPrice() == null || itemReq.unitPrice() < 0) {
                throw new IllegalArgumentException("unitPrice must be >= 0");
            }

            OrderItem item = new OrderItem(itemReq.productId(), itemReq.quantity(), itemReq.unitPrice());
            order.addItem(item);

            total += itemReq.quantity() * itemReq.unitPrice();
        }

        order.setTotal(total);
        order.setStatus(OrderStatus.CREATED);

        Order saved = orderRepository.save(order);

        // Publish only after the transaction commits, so we don't emit events for rolled-back writes.
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    orderEventsPublisher.publishOrderCreated(saved);
                }
            });
        } else {
            orderEventsPublisher.publishOrderCreated(saved);
        }

        return saved;
    }

    @Transactional
    public Order reserveStock(Long id) {
        Order order = findById(id);

        if (order.getStatus() == OrderStatus.RESERVED) {
            return order;
        }
        if (order.getStatus() == OrderStatus.CANCELLED) {
            return order;
        }

        InventoryClient.ReserveResponse reserveResponse = inventoryClient.reserve(order.getId(), order.getItems());
        OrderStatus previous = order.getStatus();

        if (reserveResponse.reserved()) {
            order.setStatus(OrderStatus.RESERVED);
        } else {
            order.setStatus(OrderStatus.CANCELLED);
        }

        Order saved = orderRepository.save(order);
        publishCancelledIfNeeded(previous, saved.getStatus(), saved);
        return saved;
    }

    @Transactional
    public Order updateStatus(Long id, OrderStatus status) {
        Order order = findById(id);
        OrderStatus previous = order.getStatus();
        order.setStatus(status);
        Order saved = orderRepository.save(order);

        publishCancelledIfNeeded(previous, status, saved);

        return saved;
    }

    private void publishCancelledIfNeeded(OrderStatus previous, OrderStatus next, Order saved) {
        boolean becameCancelled = previous != OrderStatus.CANCELLED && next == OrderStatus.CANCELLED;
        if (!becameCancelled) {
            return;
        }

        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    orderEventsPublisher.publishOrderCancelled(saved);
                }
            });
        } else {
            orderEventsPublisher.publishOrderCancelled(saved);
        }
    }
}
