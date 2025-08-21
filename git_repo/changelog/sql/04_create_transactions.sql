CREATE TABLE transactions (
    transaction_id SERIAL PRIMARY KEY,
    amount DECIMAL(15,2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    date TIMESTAMP NOT NULL,
    bic_sender VARCHAR(20),
    bic_receiver VARCHAR(20),
    account_sender VARCHAR(30),
    account_receiver VARCHAR(30),
    type_id INT,
    comments VARCHAR(100)
);