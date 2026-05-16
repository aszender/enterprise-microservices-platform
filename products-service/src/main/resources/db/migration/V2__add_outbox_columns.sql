ALTER TABLE outbox_messages ADD COLUMN IF NOT EXISTS event_id   VARCHAR(255);
ALTER TABLE outbox_messages ADD COLUMN IF NOT EXISTS topic      VARCHAR(255);
ALTER TABLE outbox_messages ADD COLUMN IF NOT EXISTS event_key  VARCHAR(255);
ALTER TABLE outbox_messages ADD COLUMN IF NOT EXISTS attempts   INTEGER NOT NULL DEFAULT 0;
ALTER TABLE outbox_messages ADD COLUMN IF NOT EXISTS last_error VARCHAR(2000);

UPDATE outbox_messages
SET event_id  = CAST(id AS VARCHAR),
    topic     = event_type,
    event_key = aggregate_id
WHERE event_id IS NULL;

ALTER TABLE outbox_messages ALTER COLUMN event_id  SET NOT NULL;
ALTER TABLE outbox_messages ALTER COLUMN topic     SET NOT NULL;
ALTER TABLE outbox_messages ALTER COLUMN event_key SET NOT NULL;

ALTER TABLE outbox_messages ADD CONSTRAINT uk_outbox_event_id UNIQUE (event_id);

CREATE INDEX IF NOT EXISTS idx_outbox_messages_aggregate ON outbox_messages (aggregate_type, aggregate_id);
