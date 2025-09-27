CREATE TABLE accounts (
    account_number VARCHAR(30) PRIMARY KEY NOT NULL,
    current_balance DECIMAL(15,2) NOT NULL DEFAULT 0,
    currency VARCHAR(3) NOT NULL,
    opened_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
        CHECK (status IN ('ACTIVE', 'BLOCKED', 'CLOSED')),
    comment VARCHAR(100)
);