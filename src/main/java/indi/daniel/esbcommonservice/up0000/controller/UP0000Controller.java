package indi.daniel.esbcommonservice.up0000.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import indi.daniel.esbcommonservice.common.CommonConst;
import indi.daniel.esbcommonservice.common.client.http.HttpClient;
import indi.daniel.esbcommonservice.common.controller.model.Header;
import indi.daniel.esbcommonservice.common.controller.model.RestServiceResponse;
import indi.daniel.esbcommonservice.common.exception.BackendInvalidResponseException;
import indi.daniel.esbcommonservice.common.utils.HeaderUtils;
import indi.daniel.esbcommonservice.up0000.UP0000Const;
import indi.daniel.esbcommonservice.up0000.controller.model.req.MockRequest;
import indi.daniel.esbcommonservice.up0000.controller.model.req.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static indi.daniel.esbcommonservice.up0000.enums.ResponseCode.SUCCESS;

@RestController
@RequestMapping("/up0000")
public class UP0000Controller {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private HttpClient httpClient;

    @Autowired
    HeaderUtils headerUtils;

    @GetMapping("/mockApi")
    public RestServiceResponse mockApi() throws IOException {
        Header header = new Header(headerUtils);
        header.setReceiverCode("UP0000");

        RequestBody requestBody = new RequestBody();
        requestBody.setMessage("api測試");

        MockRequest mockRequest = new MockRequest();
        mockRequest.setHeader(header);
        mockRequest.setRequestBody(requestBody);

        String response = httpClient.postCall(
                CommonConst.serverPrefixUrl + UP0000Const.MOCK_SUCCESS_API_URI,
                null,
                objectMapper.writeValueAsString(mockRequest));
        RestServiceResponse restServiceResponse = objectMapper.readValue(response, RestServiceResponse.class);
        if (!SUCCESS.getCode().equals(restServiceResponse.getCode())) {
            String errorMessage = String.format("%s:%s",
                    restServiceResponse.getCode(),
                    restServiceResponse.getDescription());
            throw new BackendInvalidResponseException(errorMessage);
        }
        return restServiceResponse;
    }
}
