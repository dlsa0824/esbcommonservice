package indi.daniel.esbcommonservice.common.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import indi.daniel.esbcommonservice.common.exception.InvalidJwtTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ClaimsBuilder;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtils {

    @Value("${esb.common.jwt.secret-key}")
    private String secretKeyStr;

    // @Value("${esb.common.jwt.expiration-seconds}")
    // private int validSeconds;

    @Value("${esb.common.jwt.issuer}")
    private String issuer;

    private SecretKey secretKey;

    @Autowired
    private Base64Utils base64Utils;

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    private void init() {
        this.secretKey = Keys.hmacShaKeyFor(secretKeyStr.getBytes());
    }
    
    public String generateToken(String scope, boolean validateExpiration, int validSeconds, Integer useCount, Map<String, String> claimsMap) {

        Instant now = Instant.now();

        Date issuedAt = Date.from(now);
        Date expiration = Date.from(
                now.plusSeconds(validSeconds)
        );

        // 準備 payload 內容
        ClaimsBuilder claimsBuilder = Jwts.claims()
                .issuedAt(issuedAt)
                .expiration(expiration)
                .add("issuer", issuer)
                .add("scope", scope)
                .add("validateExpiration", validateExpiration)
                .add("validSecond", validSeconds)
                .add("useCount", useCount);

        claimsMap.forEach(claimsBuilder::add);

        // 簽名後產生 JWT
        return Jwts.builder()
                .claims(claimsBuilder.build())
                .signWith(secretKey)
                .compact();
    }

    public Map<String, Object> validateToken(String token) {
        boolean validateExpiration = checkExpirationProperty(token);
        try {
            JwtParserBuilder jwtParserBuilder = Jwts.parser()
                    .verifyWith(secretKey);
            if (!validateExpiration) {
                // 設定允許的時鐘偏差為最大值，以忽略 JWT 的過期時間驗證
                jwtParserBuilder.clockSkewSeconds(Long.MAX_VALUE / 1000);
            }

            Claims claims = jwtParserBuilder
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            
            Map<String, Object> payloadMap = new HashMap<>();
            claims.forEach((key, value) -> payloadMap.put(key, value));
            return payloadMap;
        } catch (ExpiredJwtException e) {
            throw new InvalidJwtTokenException("token過期");
        } catch (SignatureException e) {
            throw new InvalidJwtTokenException("無效簽章");
        } catch (MalformedJwtException e) {
            throw new InvalidJwtTokenException("非有效jwt格式");
        } catch (UnsupportedJwtException e) {
            throw new InvalidJwtTokenException("header演算法錯誤");
        } catch (Exception e) {
            throw new InvalidJwtTokenException("無效token");
        }
    }

    private boolean checkExpirationProperty(String token) {
        try {
            String[] jwtParts = token.split("\\.");
            // if (jwtParts.length != 3) {
            //     throw new InvalidJwtTokenException("非有效jwt格式");
            // }
            String payload = new String(base64Utils.decodeUrlSafe(jwtParts[1]));
            Map<String, Object> payloadMap = objectMapper.readValue(payload, new TypeReference<Map<String, Object>>() {});

            boolean validateExpiration = (boolean) payloadMap.get("validateExpiration");

            return validateExpiration;
        } catch (Exception e) {
            throw new InvalidJwtTokenException("非有效jwt格式");
        }
    }
}
