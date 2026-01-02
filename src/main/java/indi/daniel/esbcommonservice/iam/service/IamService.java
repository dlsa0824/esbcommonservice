package indi.daniel.esbcommonservice.iam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import indi.daniel.esbcommonservice.common.utils.CredentialGenerator;
import indi.daniel.esbcommonservice.common.utils.JwtUtils;
import indi.daniel.esbcommonservice.iam.controller.model.rsp.ClientCredential;
import indi.daniel.esbcommonservice.iam.exception.InvalidClientIdOrSecretException;
import indi.daniel.esbcommonservice.iam.exception.InvalidClientScopeException;
import indi.daniel.esbcommonservice.iam.exception.ScopeWithNoApiException;
import indi.daniel.esbcommonservice.iam.repository.IamRepository;
import indi.daniel.esbcommonservice.iam.repository.model.AccessTokenDetail;
import indi.daniel.esbcommonservice.iam.repository.model.ClientScopeDetail;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class IamService {

    @Autowired
    private CredentialGenerator credentialGenerator;

    @Autowired
    private IamRepository iamRepository;

    @Autowired
    private JwtUtils jwtUtils;

    private void validateClientCredential(String clientId, String clientSecret) {
        boolean isValid = iamRepository.validateClientCredential(clientId, clientSecret);
        if (!isValid) {
            throw new InvalidClientIdOrSecretException();
        }
    }

    private void validClientScope(String clientId, String scope) {
        boolean isValid = iamRepository.validateClientScope(clientId, scope);
        if (!isValid) {
            throw new InvalidClientScopeException();
        }
    }

    public ClientCredential createClientCredential(String assetCode, boolean validateExpiration) {
        String clientId = credentialGenerator.generateClientId();
        String clientSecret = credentialGenerator.generateClientSecret();
        iamRepository.insertClientCredential(clientId, clientSecret, assetCode, validateExpiration);

        ClientCredential clientCredential = new ClientCredential();
        clientCredential.setAssetCode(assetCode);
        clientCredential.setClientId(clientId);
        clientCredential.setClientSecret(clientSecret);
        clientCredential.setValidateExpiration(validateExpiration);

        return clientCredential;
    }

    public void authorizeClientScope(String clientId, String clientSecret, String scope) {
        validateClientCredential(clientId, clientSecret);
        iamRepository.insertClientScope(clientId, scope);
    }

    public String generateJwtToken(String clientId, String clientSecret, String scope) {
        validateClientCredential(clientId, clientSecret);
        validClientScope(clientId, scope);

        List<ClientScopeDetail> clientScopeDetails = iamRepository.getClientScopeDetails(clientId, scope);

        if (clientScopeDetails.get(0).getApiPath() == null) {
            throw new ScopeWithNoApiException("scope: " + scope);
        }

        boolean validateExpiration = clientScopeDetails.get(0).isValidateExpiration();
        int validSecond = clientScopeDetails.get(0).getValidSecond();
        Integer useCount = clientScopeDetails.get(0).getUseCount();
        Map<String, String> claimsMap = new HashMap<>();
        for (ClientScopeDetail clientScopeDetail : clientScopeDetails) {
            claimsMap.put(clientScopeDetail.getApiPath(), clientScopeDetail.getMethod());
        }

        String jwtToken = jwtUtils.generateToken(scope, validateExpiration, validSecond, useCount, claimsMap);
        Map<String, Object> payloadMap = jwtUtils.validateToken(jwtToken);
        Date issuedAt = new Date((Long)payloadMap.get("iat") * 1000);
        Date expireAt = new Date((Long)payloadMap.get("exp") * 1000);

        iamRepository.insertAccessToken(clientId, jwtToken, scope, issuedAt, expireAt, validateExpiration, useCount);

        return jwtToken;
    }

    public AccessTokenDetail getAccessTokenDetail(String jwtToken) {
        List<AccessTokenDetail> accessTokenDetails = iamRepository.getAccessTokenDetail(jwtToken);
        if (accessTokenDetails.isEmpty()) {
            return null;
        } else {
            return accessTokenDetails.get(0);
        }
    }

    public void updateAccessTokenUseCount(String jwtToken) {
        iamRepository.updateAccessTokenUseCount(jwtToken);
    }

    public String validateClientCredentialAndScope(String clientId, String clientSecret, String scope) {

        return iamRepository.validateClientCredentialAndScope(clientId, clientSecret, scope);
    }
}
