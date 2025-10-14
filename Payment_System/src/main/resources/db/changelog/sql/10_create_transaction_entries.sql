CREATE TABLE transaction_entries (
    id SERIAL PRIMARY KEY,
    transaction_id BIGINT NOT NULL,
    account_number VARCHAR(30) NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    entry_date TIMESTAMP NOT NULL,
    FOREIGN KEY (transaction_id) REFERENCES transactions(transaction_id)
);
