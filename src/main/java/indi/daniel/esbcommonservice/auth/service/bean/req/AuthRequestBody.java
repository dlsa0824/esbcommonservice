package indi.daniel.esbcommonservice.auth.service.bean.req;

import lombok.Data;

@Data
public class AuthRequestBody {

    private String taxIdNumber;

    private String encryptedPassword;
}
