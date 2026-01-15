package com.aszender.spring_backend.kafka.publish;

import com.aszender.spring_backend.model.Product;

public interface ProductEventsPublisher {
    void publishProductCreated(Product product);
}
