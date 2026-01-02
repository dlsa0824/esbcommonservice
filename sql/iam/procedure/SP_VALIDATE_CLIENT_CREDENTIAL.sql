DROP PROCEDURE IF EXISTS iam.SP_VALIDATE_CLIENT_CREDENTIAL(VARCHAR, VARCHAR, BOOLEAN);

CREATE OR REPLACE PROCEDURE iam.SP_VALIDATE_CLIENT_CREDENTIAL(
    IN i_client_id VARCHAR(255),
    IN i_client_secret VARCHAR(255),
    OUT o_is_valid BOOLEAN
)
LANGUAGE plpgsql
AS $$
BEGIN
    SELECT EXISTS (
        SELECT 1
        FROM iam.TB_CLIENT_CREDENTIAL
        WHERE client_id = i_client_id
          AND client_secret = i_client_secret
    ) INTO o_is_valid;
END;
$$;