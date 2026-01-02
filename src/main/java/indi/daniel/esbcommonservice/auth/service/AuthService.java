package indi.daniel.esbcommonservice.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import indi.daniel.esbcommonservice.auth.AuthConst;
import indi.daniel.esbcommonservice.auth.service.bean.req.AuthRequest;
import indi.daniel.esbcommonservice.auth.service.bean.req.AuthRequestBody;
import indi.daniel.esbcommonservice.common.CommonConst;
import indi.daniel.esbcommonservice.common.client.http.HttpClient;
import indi.daniel.esbcommonservice.common.controller.model.Header;
import indi.daniel.esbcommonservice.common.controller.model.RestServiceResponse;
import indi.daniel.esbcommonservice.common.exception.AuthTaxIdException;
import indi.daniel.esbcommonservice.common.utils.HeaderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static indi.daniel.esbcommonservice.auth.enums.ResponseCode.SUCCESS;

@Service
public class AuthService {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    HttpClient httpClient;

    @Autowired
    HeaderUtils headerUtils;

    public void authTaxIdNumber(String taxIdNumber, String encryptedPassword) throws IOException {
        Header header = new Header(headerUtils);
        header.setReceiverCode(AuthConst.RECEIVER_CODE);

        AuthRequestBody authRequestBody = new AuthRequestBody();
        authRequestBody.setTaxIdNumber(taxIdNumber);
        authRequestBody.setEncryptedPassword(encryptedPassword);

        AuthRequest authRequest = new AuthRequest();
        authRequest.setHeader(header);
        authRequest.setRequestBody(authRequestBody);

        String response = httpClient.postCall(
                CommonConst.serverPrefixUrl + AuthConst.MOCK_AUTH_API_URI,
                null,
                objectMapper.writeValueAsString(authRequest));
        RestServiceResponse restServiceResponse = objectMapper.readValue(response, RestServiceResponse.class);

        if (!SUCCESS.getCode().equals(restServiceResponse.getCode())) {
            throw new AuthTaxIdException();
        }
    }
}
