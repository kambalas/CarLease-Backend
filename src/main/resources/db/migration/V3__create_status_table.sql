CREATE TABLE STATUS
(
    id      BIGINT,
    status  VARCHAR(30),
    isOpened    BOOLEAN DEFAULT false,
    updatedAt timestamp DEFAULT now(),
    createdAt timestamp DEFAULT now(),
    isHighRisk BOOLEAN,
    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES personal_information(id)
);
-- Function to set updated anew each time a row is updated

CREATE
OR REPLACE FUNCTION update_modified_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updatedAt
= now();
RETURN NEW;
END;
$$
LANGUAGE plpgsql;


-- apply trigger to status table

CREATE TRIGGER update_status_modtime
    BEFORE UPDATE
    ON STATUS
    FOR EACH ROW
    EXECUTE FUNCTION update_modified_column();