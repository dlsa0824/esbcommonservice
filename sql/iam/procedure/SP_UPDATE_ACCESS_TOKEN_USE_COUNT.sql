CREATE OR REPLACE PROCEDURE iam.SP_UPDATE_ACCESS_TOKEN_USE_COUNT(
    IN i_jwt_token TEXT)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE iam.TB_ACCESS_TOKEN
    SET use_count = use_count - 1
    WHERE jwt_token = i_jwt_token;
END;
$$;