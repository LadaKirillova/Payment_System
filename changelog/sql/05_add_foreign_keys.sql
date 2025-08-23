ALTER TABLE transactions
ADD CONSTRAINT fk_transactions_sender
FOREIGN KEY (bic_sender) REFERENCES payment_references(bic);

ALTER TABLE transactions
ADD CONSTRAINT fk_transactions_receiver
FOREIGN KEY (bic_receiver) REFERENCES payment_references(bic);

ALTER TABLE transactions
ADD CONSTRAINT fk_transactions_type
FOREIGN KEY (type_id) REFERENCES transaction_type(type_id);