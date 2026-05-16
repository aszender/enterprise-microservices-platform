package com.aszender.spring_backend.kafka.outbox.repository;

import com.aszender.spring_backend.kafka.outbox.model.OutboxEvent;
import com.aszender.spring_backend.kafka.outbox.model.OutboxEventStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {
    List<OutboxEvent> findTop50ByStatusOrderByCreatedAtAsc(OutboxEventStatus status);
}
