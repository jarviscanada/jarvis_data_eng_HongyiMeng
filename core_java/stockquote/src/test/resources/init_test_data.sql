DROP TABLE IF EXISTS quote;
CREATE TABLE quote (
    symbol              VARCHAR(10) PRIMARY KEY,
    open                DECIMAL(10, 2) NOT NULL,
    high                DECIMAL(10, 2) NOT NULL,
    low                 DECIMAL(10, 2) NOT NULL,
    price               DECIMAL(10, 2) NOT NULL,
    volume              INT NOT NULL,
    latest_trading_day  DATE NOT NULL,
    previous_close      DECIMAL(10, 2) NOT NULL,
    change              DECIMAL(10, 2) NOT NULL,
    change_percent      VARCHAR(10) NOT NULL,
    timestamp           TIMESTAMP DEFAULT CURRENT_TIMESTAMP  NOT NULL
);

DROP TABLE IF EXISTS position;
CREATE TABLE position (
    symbol                VARCHAR(10) PRIMARY KEY,
    number_of_shares      INT NOT NULL,
    value_paid            DECIMAL(10, 2) NOT NULL,
    CONSTRAINT symbol_fk	FOREIGN KEY (symbol) REFERENCES quote(symbol) ON DELETE CASCADE
);

INSERT INTO quote (symbol, open, high, low, price, volume, latest_trading_day, previous_close, change, change_percent)
VALUES
('AAPL', 182.50, 185.00, 181.20, 183.75, 75000000, '2025-02-19', 181.90, 1.85, '1.02%'),
('GOOG', 142.00, 145.50, 141.50, 144.25, 30000000, '2025-02-19', 140.80, 3.45, '2.45%');

INSERT INTO position (symbol, number_of_shares, value_paid)
VALUES
('AAPL', 50, 9000.00);