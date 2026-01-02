package indi.daniel.esbcommonservice.iam.controller.model.rsp;

import lombok.Data;

@Data
public class AuthorizeClientScopeRsp {

    private String clientId;

    private String clientSecret;

    private String scope;
}
