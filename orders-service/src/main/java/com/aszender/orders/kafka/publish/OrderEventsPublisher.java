package com.aszender.orders.kafka.publish;

import com.aszender.orders.model.Order;

public interface OrderEventsPublisher {
    void publishOrderCreated(Order order);
}
