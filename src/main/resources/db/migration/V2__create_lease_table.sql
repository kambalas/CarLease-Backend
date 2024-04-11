CREATE TABLE lease (
                       id BIGINT AUTO_INCREMENT,
                       make VARCHAR(50),
                       model VARCHAR(100),
                       modelVariant VARCHAR(20),
                       year CHAR(5),
                       fuelType VARCHAR(50),
                       enginePower DOUBLE,
                       url VARCHAR(2048),
                       offer VARCHAR(255),
                       terms BOOLEAN,
                       confirmation BOOLEAN,
                       carValue BIGDECIMAL,
                       period INT,
                       downPayment BIGDECIMAL,
                       residualValuePercentage DOUBLE,
                       isEcoFriendly BOOLEAN,
                       PRIMARY KEY (id)
);
