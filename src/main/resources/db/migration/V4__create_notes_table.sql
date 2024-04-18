CREATE TABLE notes
(
    id BIGSERIAL PRIMARY KEY,
    application_id BIGINT NOT NULL,
    note_text TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now();
FOREIGN KEY (application_id) REFERENCES personal_information(id)
);