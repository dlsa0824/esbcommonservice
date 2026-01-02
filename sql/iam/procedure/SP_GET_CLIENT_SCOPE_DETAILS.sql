CREATE OR REPLACE PROCEDURE iam.SP_GET_CLIENT_SCOPE_DETAILS(
    OUT o_cursor REFCURSOR,
    IN i_client_id VARCHAR DEFAULT NULL,
    IN i_scope VARCHAR DEFAULT NULL
)
LANGUAGE plpgsql
AS $$
BEGIN
    OPEN o_cursor FOR
    SELECT
        tcc.client_id,
        tcc.validate_expiration,
        tcs.scope,
        ts.valid_second,
        ts.use_count,
        tsa.method,
        tsa.api_path
    FROM
        iam.TB_CLIENT_CREDENTIAL tcc
    JOIN
        iam.TB_CLIENT_SCOPE tcs ON tcc.client_id = tcs.client_id
    JOIN
        iam.TB_SCOPE ts ON tcs.scope = ts.scope
    LEFT JOIN
        iam.TB_SCOPE_API tsa ON ts.scope = tsa.scope
    WHERE
        (i_client_id IS NULL OR tcc.client_id = i_client_id)
        AND (i_scope IS NULL OR tcs.scope = i_scope);
END;
$$;