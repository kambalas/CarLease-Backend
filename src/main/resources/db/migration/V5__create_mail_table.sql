CREATE TABLE MAIL
(
    id BIGSERIAL PRIMARY KEY,
    application_id BIGINT NOT NULL,
    mail_text TEXT,
    created_at DEFAULT now();
FOREIGN KEY (application_id) REFERENCES personal_information(id)
);