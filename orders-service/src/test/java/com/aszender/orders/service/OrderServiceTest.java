package com.aszender.orders.service;

import com.aszender.orders.dto.CreateOrderRequest;
import com.aszender.orders.dto.OrderItemRequest;
import com.aszender.orders.inventory.GrpcInventoryClient;
import com.aszender.orders.kafka.outbox.service.OrderOutboxService;
import com.aszender.orders.model.Order;
import com.aszender.orders.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderOutboxService orderOutboxService;

    @Mock
    private GrpcInventoryClient inventoryClient;

    @InjectMocks
    private OrderService orderService;

    @Test
    void create_calculatesTotalWithBigDecimalArithmetic() {
        CreateOrderRequest request = new CreateOrderRequest(
                "Ada",
                List.of(
                        new OrderItemRequest(1L, 3, new BigDecimal("0.10")),
                        new OrderItemRequest(2L, 1, new BigDecimal("0.20"))
                )
        );

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order saved = orderService.create(request);

        assertThat(saved.getTotal()).isEqualByComparingTo(new BigDecimal("0.50"));
        assertThat(saved.getItems())
                .extracting(item -> item.getUnitPrice())
                .containsExactly(new BigDecimal("0.10"), new BigDecimal("0.20"));
        verify(orderRepository).save(any(Order.class));
        verify(orderOutboxService).enqueueOrderCreated(saved);
    }
}
