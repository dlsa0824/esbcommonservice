package indi.daniel.esbcommonservice.iam.repository.model;

import lombok.Data;

@Data
public class ClientScopeDetail {
    private String clientId;
    private boolean validateExpiration;
    private String scope;
    private Integer validSecond;
    private Integer useCount;
    private String method;
    private String apiPath;
}