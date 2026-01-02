package indi.daniel.esbcommonservice.iam.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import indi.daniel.esbcommonservice.common.repository.BaseRepository;
import indi.daniel.esbcommonservice.common.repository.DatabaseAccessor;
import indi.daniel.esbcommonservice.iam.repository.model.ClientScopeDetail;
import indi.daniel.esbcommonservice.iam.repository.model.AccessTokenDetail;
import jakarta.annotation.PostConstruct;

import javax.sql.DataSource;

import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class IamRepository extends BaseRepository {

    @Autowired
    DataSource dataSource;

    @Autowired
    DatabaseAccessor databaseAccessor;

    @Value("${esb.iam.database.schema}")
    private String schema;

    private SimpleJdbcCall insertClientCredentialCall;
    private SimpleJdbcCall insertClientScopeCall;
    private SimpleJdbcCall validateClientCredentialCall;
    private SimpleJdbcCall validateClientScopeCall;
    private SimpleJdbcCall validateClientCredentialAndScopeCall;
    private SimpleJdbcCall getClientScopeDetailsCall;
    private SimpleJdbcCall insertAccessTokenCall;
    private SimpleJdbcCall getAccessTokenDetailCall;
    private SimpleJdbcCall updateAccessTokenUseCountCall;
    
    @PostConstruct
    private void init() {
        insertClientCredentialCall = new SimpleJdbcCall(dataSource)
                .withSchemaName(schema)
                .withProcedureName("SP_INSERT_CLIENT_CREDENTIAL");
                    
        validateClientCredentialCall = new SimpleJdbcCall(dataSource)
                .withSchemaName(schema)
                .withProcedureName("SP_VALIDATE_CLIENT_CREDENTIAL");

        insertClientScopeCall = new SimpleJdbcCall(dataSource)
                .withSchemaName(schema)
                .withProcedureName("SP_INSERT_CLIENT_SCOPE");

        validateClientScopeCall = new SimpleJdbcCall(dataSource)
                .withSchemaName(schema)
                .withProcedureName("SP_VALIDATE_CLIENT_SCOPE");

        validateClientCredentialAndScopeCall = new SimpleJdbcCall(dataSource)
                .withSchemaName(schema)
                .withProcedureName("SP_VALIDATE_CLIENT_CREDENTIAL_AND_SCOPE");

        getClientScopeDetailsCall = new SimpleJdbcCall(dataSource)
                .withSchemaName(schema)
                .withProcedureName("SP_GET_CLIENT_SCOPE_DETAILS")
                .declareParameters(
                        new SqlOutParameter("o_cursor", Types.REF_CURSOR, new BeanPropertyRowMapper<>(ClientScopeDetail.class)),
                        new SqlParameter("i_client_id", Types.VARCHAR),
                        new SqlParameter("i_scope", Types.VARCHAR)
                );

        insertAccessTokenCall = new SimpleJdbcCall(dataSource)
                .withSchemaName(schema)
                .withProcedureName("SP_INSERT_ACCESS_TOKEN");

        getAccessTokenDetailCall = new SimpleJdbcCall(dataSource)
                .withSchemaName(schema)
                .withProcedureName("SP_GET_ACCESS_TOKEN_DETAIL")
                .declareParameters(
                        new SqlParameter("i_jwt_token", Types.VARCHAR),
                        new SqlOutParameter("o_cursor", Types.REF_CURSOR, new BeanPropertyRowMapper<>(AccessTokenDetail.class))
                );

        updateAccessTokenUseCountCall = new SimpleJdbcCall(dataSource)
                .withSchemaName(schema)
                .withProcedureName("SP_UPDATE_ACCESS_TOKEN_USE_COUNT");
    }

    public void insertClientCredential(String clientId, String clientSecret,
                String assetCode, boolean validateExpiration) {
        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("i_client_id", clientId)
                .addValue("i_client_secret", clientSecret)
                .addValue("i_asset_code", assetCode)
                .addValue("i_validate_expiration", validateExpiration);

        databaseAccessor.callStoredProcedure(insertClientCredentialCall, in);
    }

    public boolean validateClientCredential(String clientId, String clientSecret) {
        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("i_client_id", clientId)
                .addValue("i_client_secret", clientSecret);
        Map<String, Object> out = databaseAccessor.callStoredProcedure(validateClientCredentialCall, in);
        return (Boolean) out.get("o_is_valid");
    }
    
    public void insertClientScope(String clientId, String scope) {
        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("i_client_id", clientId)
                .addValue("i_scope", scope);

        databaseAccessor.callStoredProcedure(insertClientScopeCall, in);
    }

    public boolean validateClientScope(String clientId, String scope) {
        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("i_client_id", clientId)
                .addValue("i_scope", scope);
        Map<String, Object> out = databaseAccessor.callStoredProcedure(validateClientScopeCall, in);
        return (Boolean) out.get("o_is_valid");
    }

    public String validateClientCredentialAndScope(String clientId, String clientSecret, String scope) {
        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("i_client_id", clientId)
                .addValue("i_client_secret", clientSecret)
                .addValue("i_scope", scope);

        Map<String, Object> result = databaseAccessor.callStoredProcedure(validateClientCredentialAndScopeCall, in);

        return (String) result.get("o_result_code");
    }

    public List<ClientScopeDetail> getClientScopeDetails(String clientId, String scope) {
        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("i_client_id", clientId)
                .addValue("i_scope", scope);

        Map<String, Object> result = databaseAccessor.callStoredProcedure(getClientScopeDetailsCall, in);
        
        @SuppressWarnings("unchecked")
        List<ClientScopeDetail> clientScopeDetails = (List<ClientScopeDetail>) result.get("o_cursor");
        return clientScopeDetails;
    }

    public void insertAccessToken(String clientId, String jwtToken, String scope, Date issueTime, Date expireTime, boolean validateExpiration, Integer useCount) {
        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("i_client_id", clientId)
                .addValue("i_jwt_token", jwtToken)
                .addValue("i_scope", scope)
                .addValue("i_issue_time", issueTime)
                .addValue("i_expire_time", expireTime)
                .addValue("i_validate_expiration", validateExpiration)
                .addValue("i_use_count", useCount);

        databaseAccessor.callStoredProcedure(insertAccessTokenCall, in);
    }

    public List<AccessTokenDetail> getAccessTokenDetail(String jwtToken) {
        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("i_jwt_token", jwtToken);

        Map<String, Object> result = databaseAccessor.callStoredProcedure(getAccessTokenDetailCall, in);

        @SuppressWarnings("unchecked")
        List<AccessTokenDetail> accessTokenDetails = (List<AccessTokenDetail>) result.get("o_cursor");
        return accessTokenDetails;
    }

    public void updateAccessTokenUseCount(String jwtToken) {
        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("i_jwt_token", jwtToken);

        databaseAccessor.callStoredProcedure(updateAccessTokenUseCountCall, in);
    }
}
