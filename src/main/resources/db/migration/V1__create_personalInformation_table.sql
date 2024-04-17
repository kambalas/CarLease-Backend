CREATE TABLE PERSONAL_INFORMATION (
                                     id BIGSERIAL,
                                     firstName VARCHAR(100),
                                     lastName VARCHAR(100),
                                     email VARCHAR(100),
                                     phoneNumber VARCHAR(30),
                                     pid BIGINT,
                                     dateOfBirth DATE,
                                     maritalStatus VARCHAR(50),
                                     numberOfChildren INT,
                                     citizenship VARCHAR(100),
                                     monthlyIncome NUMERIC(100, 4),
                                     PRIMARY KEY (id)
);