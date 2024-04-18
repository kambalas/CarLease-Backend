CREATE TABLE INTEREST_RATE (
                                id BIGSERIAL PRIMARY KEY,
                                regular DECIMAL(10,2) NOT NULL,
                                eco DECIMAL(10,2) NOT NULL
);

-- Function to set updated anew each time a row is updated
INSERT INTO INTEREST_RATE (regular, eco) VALUES (5.0, 4.8);