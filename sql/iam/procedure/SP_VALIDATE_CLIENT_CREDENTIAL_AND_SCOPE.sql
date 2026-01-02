CREATE OR REPLACE PROCEDURE iam.SP_VALIDATE_CLIENT_CREDENTIAL_AND_SCOPE (
    i_client_id VARCHAR,
    i_client_secret VARCHAR,
    i_scope VARCHAR,
    OUT o_result_code VARCHAR
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_client_exists BOOLEAN;
    v_scope_exists BOOLEAN;
BEGIN
    -- 檢查 client_id 和 client_secret 是否存在於 iam.TB_CLIENT_CREDENTIAL 表中
    SELECT EXISTS (
        SELECT 1
        FROM iam.TB_CLIENT_CREDENTIAL
        WHERE client_id = i_client_id AND client_secret = i_client_secret
    ) INTO v_client_exists;

    IF NOT v_client_exists THEN
        o_result_code := '0001';
    ELSE
        -- 檢查 client_id 是否有對應的 scope 存在於 iam.TB_CLIENT_SCOPE 表中
        SELECT EXISTS (
            SELECT 1
            FROM iam.TB_CLIENT_SCOPE
            WHERE client_id = i_client_id AND scope = i_scope
        ) INTO v_scope_exists;

        IF NOT v_scope_exists THEN
            o_result_code := '0002';
        ELSE
            o_result_code := '0000';
        END IF;
    END IF;
END;
$$;