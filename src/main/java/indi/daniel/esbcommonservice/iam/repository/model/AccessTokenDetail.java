package indi.daniel.esbcommonservice.iam.repository.model;

import java.util.Date;

import lombok.Data;

@Data
public class AccessTokenDetail {
    private String clientId;
    private String jwtToken;
    private String scope;
    private Date issueTime;
    private Date expireTime;
    private boolean validateExpiration;
    private Integer useCount;
}
