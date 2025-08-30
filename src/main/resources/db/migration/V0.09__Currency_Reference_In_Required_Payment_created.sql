ALTER TABLE required_payments
    ADD COLUMN currency_code VARCHAR(3);

ALTER TABLE required_payments
    ADD CONSTRAINT fk_currency
        FOREIGN KEY (currency_code)
            REFERENCES currency(code);
