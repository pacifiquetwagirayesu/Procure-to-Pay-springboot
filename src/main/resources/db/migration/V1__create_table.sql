-- USERS TABLE

CREATE SEQUENCE user_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE users
(
    id          BIGINT PRIMARY KEY DEFAULT nextval('user_seq'),
    first_name  VARCHAR(100) NOT NULL,
    last_name   VARCHAR(100),
    email       VARCHAR(255) NOT NULL UNIQUE,
    password    VARCHAR(255),
    role        VARCHAR(50),
    permissions JSONB,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_email ON users (email);

-- TOKENS TABLE


CREATE SEQUENCE token_seq_id START WITH 1 INCREMENT BY 1;

CREATE TABLE tokens
(
    id            BIGINT PRIMARY KEY DEFAULT nextval('token_seq_id'),
    access_token  VARCHAR(1000),
    refresh_token VARCHAR(1000),
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);