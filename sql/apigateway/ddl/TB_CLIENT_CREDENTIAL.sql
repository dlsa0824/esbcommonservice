
CREATE TABLE TB_CLIENT (
    client_id VARCHAR(255) PRIMARY KEY,
    client_secret VARCHAR(255) UNIQUE NOT NULL,
    valid_second INTEGER
);
