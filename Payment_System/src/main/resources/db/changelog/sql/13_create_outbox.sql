CREATE TABLE outbox (
    id SERIAL PRIMARY KEY,
    payload JSONB NOT NULL,        -- само сообщение, которое пойдёт в Kafka
    created_at TIMESTAMP DEFAULT NOW(),  -- когда создано
    sent BOOLEAN DEFAULT FALSE     -- отправлено ли уже
);
