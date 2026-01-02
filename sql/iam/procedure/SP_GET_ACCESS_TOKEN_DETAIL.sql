CREATE OR REPLACE PROCEDURE iam.SP_GET_ACCESS_TOKEN_DETAIL(
    IN i_jwt_token TEXT,
    OUT o_cursor refcursor)
LANGUAGE plpgsql
AS $$
BEGIN
    OPEN o_cursor FOR SELECT
        t.client_id,
        t.jwt_token,
        t.scope,
        t.issue_time,
        t.expire_time,
        t.validate_expiration,
        t.use_count
    FROM
        iam.TB_ACCESS_TOKEN t
    WHERE
        t.jwt_token = i_jwt_token;
END;
$$;