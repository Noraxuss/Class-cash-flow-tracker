-- ===========================
-- Create currency table
-- ===========================
CREATE TABLE currency (
                          code VARCHAR(3) NOT NULL PRIMARY KEY,
                          name VARCHAR(50) NOT NULL,
                          symbol VARCHAR(10)
);

-- ===========================
-- Insert default currencies
-- ===========================
INSERT INTO currency (code, name, symbol) VALUES
                                              ('HUF', 'Hungarian Forint', 'Ft'),
                                              ('USD', 'US Dollar', '$'),
                                              ('EUR', 'Euro', '€');
