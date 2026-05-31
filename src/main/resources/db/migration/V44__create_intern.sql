CREATE SEQUENCE IF NOT EXISTS intern_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE intern
(
    id            BIGINT       NOT NULL,
    first_name    VARCHAR(255) NOT NULL,
    email         VARCHAR(255),
    department    VARCHAR(255),
    salary        DECIMAL,
    is_remunerate BOOLEAN,
    manager_id    BIGINT,
    CONSTRAINT pk_intern PRIMARY KEY (id)
);

ALTER TABLE intern
    ADD CONSTRAINT uc_intern_email UNIQUE (email);

ALTER TABLE intern
    ADD CONSTRAINT FK_INTERN_ON_MANAGER FOREIGN KEY (manager_id) REFERENCES employee (id);