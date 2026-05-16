ALTER TABLE outbox_messages ADD COLUMN event_id VARCHAR(255);
ALTER TABLE outbox_messages ADD COLUMN topic VARCHAR(255);
ALTER TABLE outbox_messages ADD COLUMN event_key VARCHAR(255);
ALTER TABLE outbox_messages ADD COLUMN attempts INTEGER NOT NULL DEFAULT 0;
ALTER TABLE outbox_messages ADD COLUMN last_error VARCHAR(2000);

CREATE UNIQUE INDEX uk_outbox_messages_event_id ON outbox_messages (event_id);
CREATE INDEX idx_outbox_messages_aggregate ON outbox_messages (aggregate_type, aggregate_id);
