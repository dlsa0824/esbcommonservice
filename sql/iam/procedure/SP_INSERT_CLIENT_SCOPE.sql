DROP PROCEDURE IF EXISTS iam.SP_INSERT_CLIENT_SCOPE(VARCHAR, VARCHAR);

CREATE OR REPLACE PROCEDURE iam.SP_INSERT_CLIENT_SCOPE(
    IN i_client_id VARCHAR(255),
    IN i_scope VARCHAR(255)
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO iam.TB_CLIENT_SCOPE (
        client_id,
        scope
    ) VALUES (
        i_client_id,
        i_scope
    );
END;
$$;