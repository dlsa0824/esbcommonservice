DROP PROCEDURE IF EXISTS iam.SP_INSERT_ACCESS_TOKEN(VARCHAR, TEXT, VARCHAR, TIMESTAMP, TIMESTAMP, BOOLEAN, NUMERIC);

CREATE OR REPLACE PROCEDURE iam.SP_INSERT_ACCESS_TOKEN(
    i_client_id VARCHAR(255),
    i_jwt_token TEXT,
    i_scope VARCHAR(255),
    i_issue_time TIMESTAMP,
    i_expire_time TIMESTAMP,
    i_validate_expiration BOOLEAN,
    i_use_count NUMERIC
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO iam.TB_ACCESS_TOKEN (
        client_id,
        jwt_token,
        scope,
        issue_time,
        expire_time,
        validate_expiration,
        use_count
    )
    VALUES (
        i_client_id,
        i_jwt_token,
        i_scope,
        i_issue_time,
        i_expire_time,
        i_validate_expiration,
        i_use_count
    );
END;
$$;