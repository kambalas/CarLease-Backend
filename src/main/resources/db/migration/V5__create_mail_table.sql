CREATE TABLE MAIL
(
    id BIGSERIAL PRIMARY KEY,
    application_id BIGINT NOT NULL,
    mail_text TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_application_id FOREIGN KEY (application_id) REFERENCES personal_information(id)
);
