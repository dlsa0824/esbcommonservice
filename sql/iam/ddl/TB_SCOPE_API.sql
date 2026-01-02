DROP TABLE IF EXISTS iam.TB_SCOPE_API;

CREATE TABLE iam.TB_SCOPE_API (
    scope VARCHAR(255) NOT NULL,
    method VARCHAR(255) NOT NULL,
    api_path VARCHAR(255) NOT NULL,
    PRIMARY KEY (scope, method, api_path),
    FOREIGN KEY (scope) REFERENCES iam.TB_SCOPE(scope)
);
