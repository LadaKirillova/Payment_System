INSERT INTO transaction_type (type_id, type_name) VALUES (1, 'insurance');

INSERT INTO transactions (amount, currency, date, bic_sender, bic_receiver,
                         account_sender, account_receiver, type_id, comments)
VALUES (1000.00, 'EUR', '2025-01-10 14:30:00', '12345678', '87654321',
        '40817810000000012345', '40817810000000054321', 1, 'Covered loss');