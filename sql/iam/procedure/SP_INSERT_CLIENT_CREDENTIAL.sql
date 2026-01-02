DROP PROCEDURE IF EXISTS iam.SP_INSERT_CLIENT_CREDENTIAL(VARCHAR, VARCHAR, VARCHAR, BOOLEAN);

CREATE OR REPLACE PROCEDURE iam.SP_INSERT_CLIENT_CREDENTIAL(
    IN i_client_id VARCHAR(255),
    IN i_client_secret VARCHAR(255),
    IN i_asset_code VARCHAR(255),
    IN i_validate_expiration BOOLEAN
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO iam.TB_CLIENT_CREDENTIAL (
        client_id,
        client_secret,
        asset_code,
        validate_expiration
    ) VALUES (
        i_client_id,
        i_client_secret,
        i_asset_code,
        i_validate_expiration
    );
END;
$$;
