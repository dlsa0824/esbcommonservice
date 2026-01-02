DROP PROCEDURE IF EXISTS iam.SP_VALIDATE_CLIENT_SCOPE(VARCHAR, VARCHAR, BOOLEAN);

CREATE OR REPLACE PROCEDURE iam.SP_VALIDATE_CLIENT_SCOPE(
    IN i_client_id VARCHAR(255),
    IN i_scope VARCHAR(255),
    OUT o_is_valid BOOLEAN
)
LANGUAGE plpgsql
AS $$
BEGIN
    SELECT EXISTS (
        SELECT 1
        FROM iam.TB_CLIENT_SCOPE
        WHERE client_id = i_client_id
          AND scope = i_scope
    ) INTO o_is_valid;
END;
$$;