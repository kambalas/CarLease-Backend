-- CREATE TABLE LEASE (
--                        id BIGINT,
--                        make VARCHAR(50),
--                        model VARCHAR(100),
--                        modelVariant VARCHAR(20),
--                        year CHAR(5),
--                        fuelType VARCHAR(50),
--                        enginePower FLOAT,
--                        engineSize FLOAT,
--                        url VARCHAR(2048),
--                        offer BYTEA,
--                        terms BOOLEAN,
--                        confirmation BOOLEAN,
--                        carValue NUMERIC(100, 4),
--                        period INT,
--                        downPayment NUMERIC(100, 4),
--                        residualValuePercentage INT,
--                        isEcoFriendly BOOLEAN,
--                        monthlyPayment NUMERIC(100, 4),
--                        updatedAt timestamp DEFAULT now(),
--                        createdAt timestamp DEFAULT now(),
--                        PRIMARY KEY (id),
--                        FOREIGN KEY (id) REFERENCES personal_information(id)
-- );
ALTER TABLE LEASE
    ALTER COLUMN modelVariant TYPE VARCHAR(100);


ALTER TABLE LEASE ALTER COLUMN offer TYPE TEXT;
