package indi.daniel.esbcommonservice.iam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import indi.daniel.esbcommonservice.common.controller.model.RestServiceResponse;
import indi.daniel.esbcommonservice.common.utils.ExceptionUtils;
import indi.daniel.esbcommonservice.iam.exception.InvalidClientIdOrSecretException;
import indi.daniel.esbcommonservice.iam.exception.InvalidClientScopeException;
import indi.daniel.esbcommonservice.iam.exception.ScopeWithNoApiException;

import static indi.daniel.esbcommonservice.common.controller.ResponseCode.INVALID_CLIENT_ID_OR_SECRET;
import static indi.daniel.esbcommonservice.common.controller.ResponseCode.INVALID_AUTHORIZED_CLIENT_SCOPE;
import static indi.daniel.esbcommonservice.common.controller.ResponseCode.SCOPE_WITH_NO_API;

@RestControllerAdvice
@Order(0)
public class IamControllerAdviceHandler {

    @Autowired
    ExceptionUtils exceptionUtils;

    // 處理InvalidClientIdOrSecretException
    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public RestServiceResponse exceptionHandler(InvalidClientIdOrSecretException e) {
        RestServiceResponse response = new RestServiceResponse(INVALID_CLIENT_ID_OR_SECRET);
        return response;
    }

    // 處理InvalidClientScopeException
    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public RestServiceResponse exceptionHandler(InvalidClientScopeException e) {
        RestServiceResponse response = new RestServiceResponse(INVALID_AUTHORIZED_CLIENT_SCOPE);
        return response;
    }

    // 處理ScopeWithNoApiException
    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public RestServiceResponse exceptionHandler(ScopeWithNoApiException e) {
        RestServiceResponse response = new RestServiceResponse(SCOPE_WITH_NO_API);
        return response;
    }

}
