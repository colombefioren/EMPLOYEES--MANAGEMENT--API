CREATE SEQUENCE IF NOT EXISTS employee_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE employee
(
    id         BIGINT       NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    email      VARCHAR(255),
    department VARCHAR(255),
    salary     DECIMAL,
    is_active  BOOLEAN,
    CONSTRAINT pk_employee PRIMARY KEY (id)
);

ALTER TABLE employee
    ADD CONSTRAINT uc_employee_email UNIQUE (email);