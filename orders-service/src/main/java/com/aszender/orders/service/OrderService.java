package com.aszender.orders.service;

import com.aszender.orders.dto.CreateOrderRequest;
import com.aszender.orders.exception.OrderNotFoundException;
import com.aszender.orders.model.Order;
import com.aszender.orders.model.OrderItem;
import com.aszender.orders.model.OrderStatus;
import com.aszender.orders.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
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

        return orderRepository.save(order);
    }

    @Transactional
    public Order updateStatus(Long id, OrderStatus status) {
        Order order = findById(id);
        order.setStatus(status);
        return orderRepository.save(order);
    }
}
