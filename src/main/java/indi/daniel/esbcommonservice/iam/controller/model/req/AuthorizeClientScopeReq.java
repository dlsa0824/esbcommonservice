package indi.daniel.esbcommonservice.iam.controller.model.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthorizeClientScopeReq {

    @NotBlank
    private String clientId;

    @NotBlank
    private String clientSecret;

    @NotBlank
    private String scope;
}
