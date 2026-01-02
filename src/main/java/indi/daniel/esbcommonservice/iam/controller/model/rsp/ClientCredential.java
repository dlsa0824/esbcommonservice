package indi.daniel.esbcommonservice.iam.controller.model.rsp;

import lombok.Data;

@Data
public class ClientCredential {

    private String assetCode;

    private String clientId;
    
    private String clientSecret;

    private boolean validateExpiration;
}
