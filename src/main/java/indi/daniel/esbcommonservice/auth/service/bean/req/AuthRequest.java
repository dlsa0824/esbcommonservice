package indi.daniel.esbcommonservice.auth.service.bean.req;

import indi.daniel.esbcommonservice.common.controller.model.Header;
import lombok.Data;

@Data
public class AuthRequest {

    private Header header;

    private AuthRequestBody requestBody;
}
