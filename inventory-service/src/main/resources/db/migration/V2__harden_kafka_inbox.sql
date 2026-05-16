ALTER TABLE kafka_inbox_messages RENAME COLUMN received_at TO created_at;

ALTER TABLE kafka_inbox_messages ADD COLUMN status VARCHAR(32) NOT NULL DEFAULT 'RECEIVED';
ALTER TABLE kafka_inbox_messages ADD COLUMN attempts INTEGER NOT NULL DEFAULT 0;
ALTER TABLE kafka_inbox_messages ADD COLUMN last_error VARCHAR(2000);
ALTER TABLE kafka_inbox_messages ADD COLUMN processing_started_at TIMESTAMP(6) WITH TIME ZONE;
ALTER TABLE kafka_inbox_messages ADD COLUMN processed_at TIMESTAMP(6) WITH TIME ZONE;
ALTER TABLE kafka_inbox_messages ADD COLUMN failed_at TIMESTAMP(6) WITH TIME ZONE;
ALTER TABLE kafka_inbox_messages ADD COLUMN updated_at TIMESTAMP(6) WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE kafka_inbox_messages ADD COLUMN event_id VARCHAR(255);
ALTER TABLE kafka_inbox_messages ADD COLUMN event_type VARCHAR(255);
ALTER TABLE kafka_inbox_messages ADD COLUMN event_version INTEGER;
