CREATE TABLE personalInformation (
                                     id BIGINT AUTO_INCREMENT,
                                     firstName VARCHAR(100),
                                     lastName VARCHAR(100),
                                     email VARCHAR(100),
                                     phoneNumber VARCHAR(30),
                                     pid BIGINT,
                                     dateOfBirth DATE,
                                     maritalStatus VARCHAR(50),
                                     numberOfChildren INT,
                                     citizenship VARCHAR(100),
                                     monthlyIncome BIGDECIMAL,
                                     PRIMARY KEY (id)
);