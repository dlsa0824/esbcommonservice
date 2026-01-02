package indi.daniel.esbcommonservice.up0000.controller.model.req;

import indi.daniel.esbcommonservice.common.controller.model.Header;
import lombok.Data;

@Data
public class MockRequest {

    private Header header;

    private RequestBody requestBody;
}
