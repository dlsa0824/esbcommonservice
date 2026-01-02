DROP TABLE IF EXISTS iam.TB_ACCESS_TOKEN;

CREATE TABLE iam.TB_ACCESS_TOKEN (
    client_id VARCHAR(255) NOT NULL,
    jwt_token TEXT NOT NULL,
    scope VARCHAR(255) NOT NULL,
    issue_time TIMESTAMP NOT NULL,
    expire_time TIMESTAMP NOT NULL,
    validate_expiration BOOLEAN NOT NULL,
    use_count NUMERIC
);
