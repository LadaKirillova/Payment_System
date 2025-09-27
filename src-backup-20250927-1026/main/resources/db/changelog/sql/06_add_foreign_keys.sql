ALTER TABLE transactions
ADD CONSTRAINT fk_transactions_sender_account
    FOREIGN KEY (account_sender) REFERENCES accounts(account_number),
ADD CONSTRAINT fk_transactions_receiver_account
    FOREIGN KEY (account_receiver) REFERENCES accounts(account_number),
ADD CONSTRAINT fk_transactions_sender_bic
    FOREIGN KEY (bic_sender) REFERENCES payment_references(bic),
ADD CONSTRAINT fk_transactions_receiver_bic
    FOREIGN KEY (bic_receiver) REFERENCES payment_references(bic),
ADD CONSTRAINT fk_transactions_type
    FOREIGN KEY (type_id) REFERENCES transaction_type(type_id);



